package com.sm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.sm.jwt.filter.JSONWebTokenFilter;
import com.sm.service.CustomUserDetailService;

@Configuration("securityConfigBean")
@EnableWebSecurity
public class SecurityBeanConfig extends WebSecurityConfigurerAdapter{
	
	public SecurityBeanConfig() {
		System.out.println("Bean SecurityBeanConfig initialised");
	}
	
	@Autowired
	@Qualifier("customUserDetailServiceBean")
	private CustomUserDetailService userDetailService;
	
	@Autowired
	@Qualifier("jsonWebTokenFilterBean")
	private JSONWebTokenFilter jwtFilter;
	
		
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		System.out.println("SecurityBeanConfig -> configure");
		auth.userDetailsService(userDetailService);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		System.out.println("SecurityBeanConfig -> passwordEncoder");
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		System.out.println("SecurityBeanConfig -> authenticationManagerBean");
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		System.out.println("SecurityBeanConfig -> configure overloaded");
		http.csrf()
			.disable()
			.authorizeHttpRequests()
			.antMatchers("/authenticate")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.exceptionHandling()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);		
	}
}
