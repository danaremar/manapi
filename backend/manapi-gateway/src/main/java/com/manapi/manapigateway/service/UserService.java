package com.manapi.manapigateway.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manapi.manapigateway.exceptions.users.DuplicatedEmail;
import com.manapi.manapigateway.exceptions.users.DuplicatedUsername;
import com.manapi.manapigateway.model.users.Plan;
import com.manapi.manapigateway.model.users.User;
import com.manapi.manapigateway.repository.UserRepository;

@Service
public class UserService {
	
	private UserRepository userRepository;
	
	@Lazy
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Lazy
	@Autowired
	AuthenticationManager authenticationManager;

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
	
	@Transactional
	public void addUser(User user) throws DuplicatedUsername, DuplicatedEmail {
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
		user.setPlan(Plan.FREE);
		userRepository.save(user);
	}
	
	

}
