package com.cheetahapps.auth;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cheetahapps.auth.domain.Client;
import com.cheetahapps.auth.domain.GrantType;
import com.cheetahapps.auth.domain.ScopeType;
import com.cheetahapps.auth.repository.ClientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class ClientLoader implements CommandLineRunner {
	private final ClientRepository clientRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${client.accesstoken.seconds}")
	private Integer accessToken;

	@Value("${client.refreshtoken.seconds}")
	private Integer refreshToken;


	@SuppressWarnings("serial")
	@Override
	public void run(String... args) throws Exception {
		
		if (accessToken == null || refreshToken == null) {
			log.error("Access Token or Refresh Token not found :" + accessToken + refreshToken);
			throw new Exception(" Access Token or Refresh Token not found. ");
		}
		List<String> grantTypes = new ArrayList<>();
		for(GrantType grantType : GrantType.values()) {
			grantTypes.add(grantType.getCode());
		}
		
		
		Arrays.asList(
				Client.builder()
                .clientId("admin")
                .clientSecret(passwordEncoder.encode("nimda321!"))
                .grantTypes(String.join(",", grantTypes))
                .scopes(ScopeType.OPEN_ID.getCode())
                .accessTokenValiditySeconds(accessToken)
                .refreshTokenValiditySeconds(refreshToken)
                .build(),
                
                Client.builder()
                .clientId("grabdoc-web")
                .clientSecret(passwordEncoder.encode("system321!"))
                .grantTypes(String.join(",", grantTypes))
                .scopes(ScopeType.OPEN_ID.getCode())
                .accessTokenValiditySeconds(accessToken)
                .refreshTokenValiditySeconds(refreshToken)
                .build(),
                
                Client.builder()
                .clientId("grabdoc-ios")
                .clientSecret(passwordEncoder.encode("system321!"))
                .grantTypes(String.join(",", grantTypes))
                .scopes(ScopeType.OPEN_ID.getCode())
                .accessTokenValiditySeconds(accessToken)
                .refreshTokenValiditySeconds(refreshToken)
                .build(),
                
                Client.builder()
                .clientId("grabdoc-android")
                .clientSecret(passwordEncoder.encode("system321!"))
                .grantTypes(String.join(",", grantTypes))
                .scopes(ScopeType.OPEN_ID.getCode())
                .accessTokenValiditySeconds(accessToken)
                .refreshTokenValiditySeconds(refreshToken)
                .build(),
                
                Client.builder()
                .clientId("grabdoc-web")
                .clientSecret(passwordEncoder.encode("nimda321!"))
                .grantTypes(String.join(",", grantTypes))
                .scopes(ScopeType.OPEN_ID.getCode())
                .accessTokenValiditySeconds(accessToken)
                .refreshTokenValiditySeconds(refreshToken)
                .build()
				).forEach(c -> upsert(c));
		       
        
        
	}
	
	private void upsert(Client client) {
		if (isNull(clientRepository.findByClientId(client.getClientId()))) {
            log.debug("Creating default client.");
            clientRepository.save(client);
        }
	}

}
