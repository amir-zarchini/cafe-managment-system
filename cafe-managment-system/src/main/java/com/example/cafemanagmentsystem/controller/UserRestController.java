package com.example.cafemanagmentsystem.controller;

import com.example.cafemanagmentsystem.service.UserService;
import com.example.cafemanagmentsystem.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController
{
	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody Map<String, String> requestMap)
	{return userService.signUp(requestMap);}

	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap)
	{return userService.login(requestMap);}


	@GetMapping("/get")
	public ResponseEntity<List<UserWrapper>> getAllUsers()
	{return userService.getAllUsers();}

	@PostMapping("/update")
	public ResponseEntity<String> update(@RequestBody Map<String, String> requestMap)
	{return userService.update(requestMap);}


	@GetMapping("/checkToken")
	public ResponseEntity<String> checkToken() 
	{return userService.checkToken();}

	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap)
	{return userService.changePassword(requestMap);}

	@PostMapping("/forgotPassword")
	public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap)
	{return userService.forgotPassword(requestMap);}

}
