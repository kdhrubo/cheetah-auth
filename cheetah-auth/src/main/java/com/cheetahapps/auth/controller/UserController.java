package com.cheetahapps.auth.controller;

import org.springframework.web.bind.annotation.*;

import com.cheetahapps.auth.domain.User;
import com.cheetahapps.auth.dto.UserDto;
import com.cheetahapps.auth.dto.UserView;
import com.cheetahapps.auth.service.UserBusinessDelegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {

	private final UserBusinessDelegate userBusinessDelegate;

	@PostMapping("/register")
	public UserView register(@RequestBody UserDto userDto) {
		log.info("Registering user");

		
		User user = User.builder().email(userDto.getEmail()).firstName(userDto.getFirstName())
				.lastName(userDto.getLastName()).password(userDto.getPassword()).tenantName(userDto.getCompany()).build();
		
		User u = this.userBusinessDelegate.register(user, userDto.getCompany(), userDto.getCountry());
		
		return new UserView(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getCreatedDate(), null);
	}
	
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
