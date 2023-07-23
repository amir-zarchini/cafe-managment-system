package com.example.cafemanagmentsystem.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter
{
	private final JwtUtil jwtUtil;
	private final CustomerUserDetailsService service;
	
	Claims claims=null;
	private String userName=null;
	
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
			throws ServletException, IOException 
	{
		if(httpServletRequest.getServletPath().matches("/user/login|/user/forgetPassword|/user/signup"))
		{
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
		else
		{
			String authorizationHeader = httpServletRequest.getHeader("Authorization");
			String token=null;
			
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
			{
				token=authorizationHeader.substring(7);
				userName=jwtUtil.extractUsername(token);
				claims=jwtUtil.extractAllClaims(token);
			}
			if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null)
			{
				UserDetails userDetails=service.loadUserByUsername(userName);
				if(jwtUtil.validateToken(token, userDetails))
				{
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
							new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
					
					usernamePasswordAuthenticationToken.setDetails(
							new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
					
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			filterChain.doFilter(httpServletRequest, httpServletResponse); 
		}
	}
	
	//to check whether user is admin or not
	public boolean isAdmin()
	{
		return "admin".equalsIgnoreCase((String)claims.get("role"));
	}
	
	//to check whether user is admin or not
	public boolean isUser()
	{
		return "user".equalsIgnoreCase((String)claims.get("role"));
	}
	
	//to return the current user name
	public String getCurrentUser()
	{
		return userName;
	}
}
