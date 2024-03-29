package com.manapi.manapigateway.jwt;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.manapi.manapigateway.model.users.User;
import com.manapi.manapigateway.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserService userService;

	@Autowired(required = true)
	protected ModelMapper modelMapper;
	
	@Override
	public PrincipalUser loadUserByUsername(String username) throws UsernameNotFoundException {
		User user =  userService.findUserByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("User not found");
		}
		return PrincipalUser.build(user);
	}

}
