package com.sm.service;

import com.sm.entity.UserEntity;
import com.sm.repository.UserEntityRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailServiceBean")
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserEntityRepository userEntityRepository;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userEntityRepository.findByUsername(username);
		return new User(userEntity.getUsername(), userEntity.getPassword(), new ArrayList<>());
	}
	
	public List<UserEntity> getAllUsers() throws Exception{
		return userEntityRepository.findAll();
	}
	
}
