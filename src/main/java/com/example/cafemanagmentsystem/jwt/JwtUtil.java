package com.example.cafemanagmentsystem.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil 
{
	private String secret="btechdays";
	
	//extract username from the token
	public String extractUsername(String token)
	{
		return extractClaims(token, Claims::getSubject);
	}
	
	//get token expiration
	public Date extractExpiration(String token)
	{
		return extractClaims(token, Claims::getExpiration);
	}
	
	//extract claims from the token
	public <T> T extractClaims(String token,Function<Claims, T> claimsResolver)
	{
		final Claims claims=extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	//extract all claims from the token
	public Claims extractAllClaims(String token)
	{
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	//to check token which user has provided is expired or not
	private Boolean isTokenExpired(String token)
	{
		return extractExpiration(token).before(new Date());
	}
	
	//generate token
	public String generateToken(String username,String role)
	{
		Map<String, Object> claims= new HashMap<>();
		claims.put("role", role);
		return createToken(claims, username);
	}
	
	//to create token
	private String createToken(Map<String, Object> claims,String subject)
	{
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}
	
	//to validate the token
	public Boolean validateToken(String token,UserDetails userDetails)
	{
		final String username=extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
