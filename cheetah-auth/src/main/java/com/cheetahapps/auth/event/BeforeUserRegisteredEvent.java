package com.cheetahapps.auth.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeforeUserRegisteredEvent {
	
	private String company;
	private String country;
	private String tenantId;
	private String tenantCode;
	
	private boolean existingTenant;
	
}
