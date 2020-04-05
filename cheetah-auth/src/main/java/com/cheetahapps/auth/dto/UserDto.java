package com.cheetahapps.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserDto {
	
	@NotEmpty
	private String firstName;
	
	@NotEmpty
	private String lastName;
	
	@NotEmpty
	@Email
	private String email;
	
	@NotEmpty
	@Min(8)
	@Max(12)
	private String password;
	
	@NotEmpty
	private String company;
	
	@NotEmpty
	private String country;
	
	

}
