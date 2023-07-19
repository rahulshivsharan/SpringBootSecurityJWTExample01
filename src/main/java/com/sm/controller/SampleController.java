package com.sm.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sm.entity.AuthenticationRequest;
import com.sm.entity.UserEntity;
import com.sm.jwt.filter.JSONWebTokenFilter;
import com.sm.jwt.util.JSONWebTokenUtil;
import com.sm.service.CustomUserDetailService;

@RestController
@RequestMapping("api/v1/data")
public class SampleController {
	
	
	@Autowired
	@Qualifier("jwtUtilBean")
	private JSONWebTokenUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	@Qualifier("customUserDetailServiceBean")
	private CustomUserDetailService userService;

	@GetMapping(value="/hi",produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String sayHi() {
		System.out.println("Calling Hi");
		String str = "Hi from first application";
		return str;
	}
	
	@PostMapping("/authenticate")
	public String generateToken(@RequestBody AuthenticationRequest authReq) throws Exception{
		System.out.println("generateToken API 1");
		String token = null;
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
			System.out.println("generateToken API 2");
			token = jwtUtil.generateToken(authReq.getUsername());
			System.out.println("generateToken API 3");
			System.out.println("TOKEN "+token);
		}catch(Exception e) {
			System.out.println(e);
			throw new Exception("invalid Username/Password",e);
		}
		
		return token;
	}
	
	
	@GetMapping(value="/getUsers",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserEntity> getAllUsers() throws Exception{
		List<UserEntity> users = null;
		try {
			users =  userService.getAllUsers();
		}catch(Exception e) {
			String msg = "Exception in getAllusers "+e.getMessage();
			throw new Exception (msg);
		}
		
		return users;
	}	
	
}
