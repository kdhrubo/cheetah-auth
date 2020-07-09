package com.cheetahapps.auth.config;

import lombok.RequiredArgsConstructor;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import com.cheetahapps.auth.domain.User;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.nonNull;


@RequiredArgsConstructor
public class AuthTokenConverter extends DefaultUserAuthenticationConverter {
	
	

	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {
		
		User user = (User) authentication.getPrincipal();
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("sub", user.getEmail());
		response.put("userId", user.getId());
		response.put("firstName", user.getFirstName());
		response.put("lastName", user.getLastName());
		response.put("tenantId", user.getTenantId());
		response.put("tenantCode", user.getTenantCode());
		response.put("iss", "http://localhost:7001/auth/issuer"); // remove hard coding
		
		mapAuthorities(authentication, response);
		
		return response;
	}
	
	

	private void mapAuthorities(Authentication authentication, Map<String, Object> response) {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		if (nonNull(authorities) && !authorities.isEmpty()) {
			response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authorities));
		}
	}

}
