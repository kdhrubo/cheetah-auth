package com.cheetahapps.auth.controller;

import org.springframework.web.bind.annotation.*;

import com.cheetahapps.auth.domain.User;
import com.cheetahapps.auth.service.UserBusinessDelegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {

	private final UserBusinessDelegate userBusinessDelegate;
	
	
	/*
	@PostMapping("/otp/{email}")
	public UserView generateVerificationCode(@PathVariable String email) throws Exception {
		User u = userService.generateOtp(email);
		return new UserView(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getCreatedDate(), null);
	}

	@PostMapping("/changepassword")
	public UserView updatePassword(@Valid @RequestBody User user) {
		User u = userService.updatePassword(user.getEmail(), user.getVerificationCode(), user.getPassword());

		return new UserView(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getCreatedDate(), null);

	}*/
	
}
