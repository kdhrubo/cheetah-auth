package com.cheetahapps.auth.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document
public class Role {
	
	public static final String USER = "ROLE_USER";
	
	@Id
	private int id;
	
	private String name;
    
}