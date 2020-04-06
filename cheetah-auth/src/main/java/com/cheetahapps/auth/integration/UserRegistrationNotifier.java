package com.cheetahapps.auth.integration;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cheetahapps.auth.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//TODO - This is a temporary solution must be replaced with messaging
//TODO - Creates circular dependency

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationNotifier {

	@Value("${notify.url}")
	private String provisionUrl;

	public void notify(User user) {
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
	    headers.set("X-TENANT-ID", user.getTenant().getCode());    
	    
	    HttpEntity<User> request = new HttpEntity<>(user, headers);
		
		ResponseEntity<String> result = restTemplate.postForEntity(provisionUrl, request, String.class);
		
		log.info("Provision status - {}", result.getStatusCode());
	}

}
