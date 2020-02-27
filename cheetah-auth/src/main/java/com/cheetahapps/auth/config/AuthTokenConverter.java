package com.cheetahapps.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import com.cheetahapps.auth.domain.User;
import com.cheetahapps.auth.service.UserService;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
public class AuthTokenConverter extends DefaultUserAuthenticationConverter {

	private final UserService userService;

	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {
		
		log.info("convertUserAuthentication - {}" , authentication.getPrincipal().getClass());
		User user = (User) authentication.getPrincipal();
		String username = authentication.getName();
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("sub", username);
		response.put("user_id", user.getId());
		
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
