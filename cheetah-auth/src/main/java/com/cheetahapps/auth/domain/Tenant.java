package com.cheetahapps.auth.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document("Tenant")
@TypeAlias("Tenant")
public class Tenant {
	
	@Id
	private String id;
	
	@Indexed(unique = true)
	private String name;
	
	private long sequence;
	
	private	Address address;
	
	private boolean deleted;
	
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	
	@CreatedDate
	private LocalDateTime createdDate;

}
