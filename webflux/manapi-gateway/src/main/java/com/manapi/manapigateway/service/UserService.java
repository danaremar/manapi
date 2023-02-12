package com.manapi.manapigateway.service;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manapi.manapigateway.model.user.User;
import com.manapi.manapigateway.repository.UserRepository;
import com.manapi.manapigateway.dto.UserCreateDto;
import com.manapi.manapigateway.dto.UserShowDto;
import com.manapi.manapigateway.model.subscription.Subscription;

import jakarta.validation.Valid;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = true)
    protected ModelMapper modelMapper;

    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;

    // TODO: get from authentication manager
    public String getCurrentUsername() {
        return "JEJE";
    }

    public User getCurrentUser() {
        return findUserByUsername(getCurrentUsername());
    }

    public User findUserById(String id) {
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

    public UserShowDto showMyProfile() {
        User user = getCurrentUser();
        return modelMapper.map(user, UserShowDto.class);
    }

    @Transactional
    public void addUser(@Valid UserCreateDto userCreateDto) {
        User user = modelMapper.map(userCreateDto, User.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setCreationDate(new Date());
        user.setFailedRetries(0L);
        
        Subscription subscription = new Subscription();
        subscription.setCreationDate(new Date());
        subscription.setStartDate(new Date());
        subscription.setPlan("FREE");
        subscription.setFeatureGroups(List.of("PROJECTS"));
        user.setSubscriptions(List.of(subscription));

        userRepository.save(user);
    }
    
}
