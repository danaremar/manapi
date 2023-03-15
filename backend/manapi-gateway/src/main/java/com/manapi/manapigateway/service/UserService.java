package com.manapi.manapigateway.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.manapi.manapigateway.exceptions.users.DuplicatedEmail;
import com.manapi.manapigateway.exceptions.users.DuplicatedUsername;
import com.manapi.manapigateway.exceptions.users.IncorrectPassword;
import com.manapi.manapigateway.exceptions.users.UserNotFound;
import com.manapi.manapicommon.model.users.PlanType;
import com.manapi.manapigateway.model.users.User;
import com.manapi.manapicommon.model.users.UserCreateDto;
import com.manapi.manapicommon.model.users.UserShowDto;
import com.manapi.manapicommon.model.users.UserUpdateDto;
import com.manapi.manapigateway.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired(required = true)
	protected ModelMapper modelMapper;
	
	@Lazy
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Lazy
	@Autowired
	AuthenticationManager authenticationManager;

	public String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}

	public User getCurrentUser() {
		return findUserByUsername(getCurrentUsername());
	}

	public User findUserById(Long id) {
		User userExample = new User();
		userExample.setId(id);
		userExample.setActive(true);
		Example<User> example = Example.of(userExample);
		return userRepository.findOne(example).orElse(null);	
	}
	
	public User findUserByUsername(String username) {
		User userExample = new User();
		userExample.setUsername(username);
		userExample.setActive(true);
		Example<User> example = Example.of(userExample);
		return userRepository.findOne(example).orElse(null);	
	}
	
	public User findUserByEmail(String email) {
		User userExample = new User();
		userExample.setEmail(email);
		userExample.setActive(true);
		Example<User> example = Example.of(userExample);
		return userRepository.findOne(example).orElse(null);	
	}

	public UserShowDto showUserByUsername(String username) {
		User user = findUserByUsername(username);
		return modelMapper.map(user, UserShowDto.class);
	}

	public UserShowDto showMyProfile() {
		User user = getCurrentUser();
		return modelMapper.map(user, UserShowDto.class);
	}
	
	@Transactional
	public void addUser(@Valid UserCreateDto userDto) throws DuplicatedUsername, DuplicatedEmail {
		User user = modelMapper.map(userDto, User.class);
		if (findUserByUsername(user.getUsername()) != null) {
			throw new DuplicatedUsername();
		}
		if (findUserByEmail(user.getEmail()) != null) {
			throw new DuplicatedEmail();
		}
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		user.setActive(true);
		user.setCreationDate(new Date());
		user.setLastConnection(new Date());
		user.setFailedRetries(0L);
		user.setPlan(PlanType.FREE);
		userRepository.save(user);
	}

	@Transactional
	public void updateUser(UserUpdateDto user) throws UserNotFound, DuplicatedEmail, DuplicatedUsername, AuthenticationException, IncorrectPassword {
		String username = getCurrentUsername();
		User userBefore = findUserByUsername(username);
		
		if (userBefore == null) {
			throw new UserNotFound();
		}
		if (!user.getUsername().equals(userBefore.getUsername()) && findUserByUsername(user.getUsername()) != null) {
			throw new DuplicatedUsername();
		}
		if (!user.getEmail().equals(userBefore.getEmail()) && findUserByEmail(user.getEmail()) != null) {
			throw new DuplicatedEmail();
		}
		
		// verify password
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					username, user.getOldPassword(), Collections.emptyList()));
		} catch (AuthenticationException e) {
			throw new IncorrectPassword();
		}

		userBefore.setUsername(user.getUsername());
		userBefore.setName(user.getName());
		userBefore.setLastName(user.getLastName());
		userBefore.setEmail(user.getEmail());
		if(StringUtils.isNotBlank(user.getPassword())) {
			String newCypheredPassword = this.passwordEncoder.encode(user.getPassword());
			userBefore.setPassword(newCypheredPassword);
		}
		userBefore.setCountry(user.getCountry());
		userBefore.setSector(user.getSector());
		userBefore.setLastConnection(new Date());
		
		userRepository.save(userBefore);
	}

	@Transactional
	public void deleteUser() throws UserNotFound {
		User user = getCurrentUser();
		if (user == null) {
			throw new UserNotFound();
		}
		user.setLastConnection(new Date());
		user.setDeleteDate(new Date());
		user.setActive(false);
		userRepository.save(user);
	}

	@Transactional
	public void uploadImage(MultipartFile image) {
		User user = getCurrentUser();
		String path = FileUtils.IMAGES_PATH;
		
		// delete from path previous image
		if(StringUtils.isNotBlank(user.getImageUid())) {
			try {
				deleteImage(path, user);
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// create new image & update
		String newImageUid = FileUtils.getFileNameUID(image);
		try {
			FileUtils.uploadToPath(image, path, newImageUid);
			user.setImageUid(newImageUid);
			userRepository.save(user);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@Transactional
	public void deleteImage() {
		String path = FileUtils.IMAGES_PATH;
		deleteImage(path, getCurrentUser());
	}
	
	@Transactional
	public void deleteImage(String path, User user) {
		try {
			if(!user.getImageUid().contains("cute") && !user.getImageUid().contains("meme") && !user.getImageUid().contains("chibi")) {
				FileUtils.deleteFromPath(path, user.getImageUid());
			}
			user.setImageUid(null);
			userRepository.save(user);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
