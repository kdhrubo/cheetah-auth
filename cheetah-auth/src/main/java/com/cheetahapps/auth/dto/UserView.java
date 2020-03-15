package com.cheetahapps.auth.dto;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Value;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
public class UserView {
	
	String id,firstName,lastName, email;
	
	LocalDateTime createdDate, lastLoginDate;

	
}
