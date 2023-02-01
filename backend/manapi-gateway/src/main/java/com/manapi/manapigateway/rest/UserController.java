package com.manapi.manapigateway.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.manapi.manapigateway.config.ManapiMessages;
import com.manapi.manapigateway.exceptions.users.DuplicatedEmail;
import com.manapi.manapigateway.exceptions.users.DuplicatedUsername;
import com.manapi.manapigateway.exceptions.users.IncorrectPassword;
import com.manapi.manapigateway.exceptions.users.UserNotFound;
import com.manapi.manapigateway.model.users.UserShowDto;
import com.manapi.manapigateway.model.users.UserUpdateDto;
import com.manapi.manapigateway.model.util.Message;
import com.manapi.manapigateway.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/profile")
@Tag(name = "Users")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    private ResponseEntity<Object> userNotFoundResponse() {
		return new ResponseEntity<>(new Message(ManapiMessages.USER_NOT_FOUND_MESSAGE), HttpStatus.NOT_FOUND);
	}

    @GetMapping
	public ResponseEntity<Object> getMyProfile() {
		UserShowDto user = userService.showMyProfile();
		if(user!=null) {
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			return userNotFoundResponse();
		}
	}

    @GetMapping(value = "/{user_username}")
	public ResponseEntity<Object> getUserByUsername(@PathVariable(name = "user_username") String username) {
		try {
			UserShowDto user = userService.showUserByUsername(username);
			if (user != null) {
				return new ResponseEntity<>(user, HttpStatus.OK);
			} else {
				return userNotFoundResponse();
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @PutMapping
	public ResponseEntity<Object> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto) {
		try {
			userService.updateUser(userUpdateDto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (UserNotFound e) {
			return userNotFoundResponse();
		} catch (DuplicatedEmail e) {
			return new ResponseEntity<>(new Message(ManapiMessages.USER_DUPLICATED_EMAIL_MESSAGE), HttpStatus.CONFLICT);
		} catch (DuplicatedUsername e) {
			return new ResponseEntity<>(new Message(ManapiMessages.USER_USERNAME_DUPLICATED_MESSAGE), HttpStatus.CONFLICT);
		} catch (IncorrectPassword e) {
			return new ResponseEntity<>(new Message(ManapiMessages.USER_OLD_PASSWORD_INCORRECT), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @DeleteMapping
	public ResponseEntity<Object> deleteUserById() {
		try {
			userService.deleteUser();
			return new ResponseEntity<>(new Message(ManapiMessages.USER_DISABLED_MESSAGE), HttpStatus.OK);
		} catch (UserNotFound e) {
			return new ResponseEntity<>(new Message(ManapiMessages.USER_NOT_FOUND_MESSAGE), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @PostMapping(value = "/image")
	public ResponseEntity<Object> uploadImage(@RequestParam("image") MultipartFile image) {
		try {
			userService.uploadImage(image);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message(ManapiMessages.IMAGE_CANNOT_UPDATED), HttpStatus.CONFLICT);
		}
	}
	
	@DeleteMapping(value = "/image")
	public ResponseEntity<Object> deleteImage() {
		try {
			userService.deleteImage();
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new Message(ManapiMessages.IMAGE_CANNOT_DELETED), HttpStatus.CONFLICT);
		}
	}

}
