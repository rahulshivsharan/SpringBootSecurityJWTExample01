package com.sm.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sm.jwt.util.JSONWebTokenUtil;
import com.sm.service.CustomUserDetailService;

@Component("jsonWebTokenFilterBean")
public class JSONWebTokenFilter extends OncePerRequestFilter{
	
	
	private static String AUTH_HEADER = "Authorization";
	private static String BEARER_HEADER = "Bearer ";
	
	public JSONWebTokenFilter() {
		System.out.println("JSONWebTokenFilter initialised");
	}
	
	@Autowired
	@Qualifier("jwtUtilBean")
	private JSONWebTokenUtil jwtUtil;

	@Autowired
	@Qualifier("customUserDetailServiceBean")
	private CustomUserDetailService service;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println("JSONWebTokenFilter doFilterInternal...");
		
		String authorizationHeader = request.getHeader(JSONWebTokenFilter.AUTH_HEADER);
		String token = null;
		String username = null;
		
		System.out.println("JSONWebTokenFilter.doFilterInternal authorizationHeader "+authorizationHeader);
		
		
		if (authorizationHeader != null && authorizationHeader.startsWith(JSONWebTokenFilter.BEARER_HEADER)) {
			token = authorizationHeader.substring(7);
			System.out.println("JSONWebTokenFilter.doFilterInternal token1 "+token);
			username = jwtUtil.extractUsername(token);
		}
		
		System.out.println("JSONWebTokenFilter.doFilterInternal token2 "+token);
		System.out.println("JSONWebTokenFilter.doFilterInternal username "+username);
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = service.loadUserByUsername(username);
			
			if(jwtUtil.validateToken(token, userDetails)) {
				
				UsernamePasswordAuthenticationToken usernamepasswordAuthToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				WebAuthenticationDetails details =  new WebAuthenticationDetailsSource().buildDetails(request);
				
				usernamepasswordAuthToken.setDetails(details);
				
				SecurityContext context = SecurityContextHolder.getContext(); 
				
				context.setAuthentication(usernamepasswordAuthToken);
			}
			
			filterChain.doFilter(request, response);
		}
	}

}
