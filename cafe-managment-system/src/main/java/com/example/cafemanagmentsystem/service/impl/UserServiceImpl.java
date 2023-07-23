package com.example.cafemanagmentsystem.service.impl;

import com.example.cafemanagmentsystem.constents.CafeConstants;
import com.example.cafemanagmentsystem.jwt.CustomerUserDetailsService;
import com.example.cafemanagmentsystem.jwt.JwtFilter;
import com.example.cafemanagmentsystem.jwt.JwtUtil;
import com.example.cafemanagmentsystem.model.User;
import com.example.cafemanagmentsystem.repository.UserRepository;
import com.example.cafemanagmentsystem.service.UserService;
import com.example.cafemanagmentsystem.utils.CafeUtils;
import com.example.cafemanagmentsystem.utils.EmailUtils;
import com.example.cafemanagmentsystem.wrapper.UserWrapper;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final CustomerUserDetailsService customerUserDetailsService;
	private final JwtUtil jwtUtil;
	private final JwtFilter jwtFilter;
	private final EmailUtils emailUtils;

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) 
	{
		log.info("Inside Signup {}",requestMap);
		try
		{
			if(validateSignUpMap(requestMap))
			{
				User user=userRepository.findByEmailId(requestMap.get("email"));
				if(Objects.isNull(user))
				{
					userRepository.save(getUserFromMap(requestMap));
					return CafeUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
				}
				else
				{
					return CafeUtils.getResponseEntity("Email already exist.",HttpStatus.BAD_REQUEST);
				}
			}
			else
			{
				return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//validating the request data
	private boolean validateSignUpMap(Map<String, String> requestMap)
	{
		return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
				&& requestMap.containsKey("email") && requestMap.containsKey("password");
	}
	
	//return user object method
	private User getUserFromMap(Map<String, String> requestMap)
	{
		User user=new User();
		user.setName(requestMap.get("name"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false");
		user.setRole("user");
		
		return user;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) 
	{
		log.info("Inside Login");
		try 
		{
			Authentication auth=authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
					);
			
			if(auth.isAuthenticated())
			{
				if(customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true"))
				{
					return new ResponseEntity<>("{\"token\":\""+
							jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
									customerUserDetailsService.getUserDetail().getRole()) + "\"}",
							HttpStatus.OK);
				}
				else
				{
					return new ResponseEntity<>("{\"message\":\""+"Wait for admin approval."+"\"}",
							HttpStatus.BAD_REQUEST);
				}
			}
			
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return new ResponseEntity<>("{\"message\":\""+"Bad Credentials."+"\"}",
				HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUsers()
	{
		try 
		{
			return jwtFilter.isAdmin()
					? new ResponseEntity<>(userRepository.getAllUser(),HttpStatus.OK)
					: new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) 
	{
		try 
		{
			if(jwtFilter.isAdmin())
			{
				Optional<User> optional = userRepository.findById(Integer.parseInt(requestMap.get("id")));
				
				if(optional.isPresent())
				{
					userRepository.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
					sendMailToAdmin(requestMap.get("status"),optional.get().getEmail(),userRepository.getAllAdmin());
					return CafeUtils.getResponseEntity("User Status Updated Successfully.", HttpStatus.OK);
				}
				else
				{
					return CafeUtils.getResponseEntity("User id doesn't exist.", HttpStatus.OK);
				}
			}
			else
			{
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAdmin(String status, String user, List<String> allAdmin)
	{
		allAdmin.remove(jwtFilter.getCurrentUser());
		if (status != null && status.equalsIgnoreCase("true")) {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),
					"Account Approved", "USER:-" + user
							+ "\n is approved by \nADMIN:-" +
							jwtFilter.getCurrentUser(), allAdmin);
		} else {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),
					"Account Disabled", "USER:-" + user
							+ "\n is disabled by \nADMIN:-" +
							jwtFilter.getCurrentUser(), allAdmin);
		}
	}

	@Override
	public ResponseEntity<String> checkToken() {return CafeUtils.getResponseEntity("true", HttpStatus.OK);}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) 
	{
		try 
		{
			User userObj = userRepository.findByEmail(jwtFilter.getCurrentUser());
			if(userObj != null)
			{
				if(userObj.getPassword().equals(requestMap.get("oldPassword")))
				{
					userObj.setPassword(requestMap.get("newPassword"));
					userRepository.save(userObj);
					return CafeUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
				}
				return CafeUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
			}
			return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) 
	{
		try 
		{
			User user = userRepository.findByEmail(requestMap.get("email"));
			if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
			{
				emailUtils.forgotMail(user.getEmail(), "Credentials by Cafe Management System", user.getPassword());
			}
			return CafeUtils.getResponseEntity("Check your mail for credentials.", HttpStatus.OK);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();		
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
