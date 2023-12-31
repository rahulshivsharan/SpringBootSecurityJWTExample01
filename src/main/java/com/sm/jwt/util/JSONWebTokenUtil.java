package com.sm.jwt.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service("jwtUtilBean")
public class JSONWebTokenUtil {
	private String secret = "tiptopsecret@123";
	
	public String extractUsername(String token){
		return extractClaim(token, Claims :: getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims :: getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	private String createToken(Map<String, Object> claims, String subject) {
		Date tokenStartDate = new Date(System.currentTimeMillis());
		Date tokenExpiryDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10);
		
		JwtBuilder jwtBuilder = Jwts.builder()
									.setClaims(claims)
									.setSubject(subject);
		 
		String token = jwtBuilder.setIssuedAt(tokenStartDate)
									.setExpiration(tokenExpiryDate)
									.signWith(SignatureAlgorithm.HS256, secret)
									.compact();
		
		return token;
	}
	
	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
