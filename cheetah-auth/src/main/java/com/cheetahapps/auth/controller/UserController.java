package com.cheetahapps.auth.controller;

import javax.validation.Valid;


import org.springframework.web.bind.annotation.*;

import com.cheetahapps.auth.domain.User;
import com.cheetahapps.auth.dto.UserView;
import com.cheetahapps.auth.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
	
	private final UserService userService;
	
	
	@PostMapping("/register")
	public UserView register(@RequestBody User user) {
		log.info("Registering user");
		User u = this.userService.register(user);
		return new UserView(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getCreatedDate(), null);
	}

	
	
	@PostMapping("/otp/{email}")
	public UserView generateVerificationCode(@PathVariable String email) throws Exception{
		User u = userService.generateOtp(email);
		return new UserView(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getCreatedDate(), null);
	}
	
	@PostMapping("/changepassword") 
	public UserView updatePassword(@Valid @RequestBody User user) {
		User u = userService.updatePassword(user.getEmail(), user.getVerificationCode(), user.getPassword());
		
		return new UserView(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getCreatedDate(), null);
		
	}
}
