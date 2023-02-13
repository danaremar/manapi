package com.manapi.manapigateway.service;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.manapi.manapigateway.model.user.User;
import com.manapi.manapigateway.repository.UserRepository;

import reactor.core.publisher.Mono;

import com.manapi.manapigateway.configuration.ManapiMessages;
import com.manapi.manapigateway.dto.UserCreateDto;
import com.manapi.manapigateway.dto.UserLoginDto;
import com.manapi.manapigateway.dto.UserShowDto;
import com.manapi.manapigateway.model.subscription.Subscription;
import com.manapi.manapigateway.jwt.JwtService;
import com.manapi.manapigateway.jwt.PrincipalUser;
import com.manapi.manapigateway.jwt.JwtDto;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import javax.validation.Valid;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired(required = true)
    protected ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    // REACTIVE
    public Mono<String> getCurrentUsername() {
        return ReactiveSecurityContextHolder.getContext().map(x -> x.getAuthentication().getName());
    }

    public Mono<User> getCurrentUser() {
        return getCurrentUsername().map(x -> findUserByUsername(x));
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

    public Mono<UserShowDto> showMyProfile() {
        return getCurrentUser().map(x -> modelMapper.map(x, UserShowDto.class));
    }

    public void failedRetry(User user) {
        user.setFailedRetries(user.getFailedRetries() + 1);
        userRepository.save(user);
    }

    @Transactional
    public JwtDto getJwtFromUser(UserLoginDto userLoginDto) {

        User user = findUserByUsername(userLoginDto.getUsername());
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ManapiMessages.USER_NOT_FOUND_MESSAGE);
        }
        PrincipalUser principalUser = PrincipalUser.build(user);

        String passToCheck = this.passwordEncoder.encode(userLoginDto.getPassword());
        if (!passToCheck.equals(user.getPassword())) {
            failedRetry(user);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ManapiMessages.USER_OLD_PASSWORD_INCORRECT);
        }

        String jwt = this.jwtService.generateToken(user);

        JwtDto jwtDto = new JwtDto();
        jwtDto.setToken(jwt);
        jwtDto.setUsername(principalUser.getUsername());
        jwtDto.setAuthorities(principalUser.getAuthorities());

        return jwtDto;
    }

    @Transactional
    public void addUser(@Valid UserCreateDto userCreateDto) {
        User user = modelMapper.map(userCreateDto, User.class);

        // TODO: check if duplicated username or duplicated email

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
