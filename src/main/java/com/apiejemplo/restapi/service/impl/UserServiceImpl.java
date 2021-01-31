package com.apiejemplo.restapi.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.apiejemplo.restapi.entity.UserDB;
import com.apiejemplo.restapi.repository.UserRepository;


@Service("userService")
public class UserServiceImpl implements UserDetailsService{
	
	
	@Autowired
	@Qualifier("userRepository")
	UserRepository userRepository;
	

	/**
	 * Load user by username.
	 *
	 * @param username the username
	 * @return the user
	 * @throws UsernameNotFoundException the username not found exception
	 */
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDB user = userRepository.findByUsername(username);
		return new User(user.getUsername(), user.getPassword(), new ArrayList());
	}


}
