package com.cheetahapps.auth.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document
public class Otp {
	@Id
	private String id;
	private String mobileNo;
	private int otp;
	private Date createdDate;
	
	
}
