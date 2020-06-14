
package com.cheetahapps.auth.config;

import java.security.KeyPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.cheetahapps.auth.client.ClientRepository;
import com.cheetahapps.auth.service.UserBusinessDelegate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	private final AuthenticationManager authenticationManager;
	private final KeyPair keyPair;
	private final ClientRepository clientRepository;
	private final UserBusinessDelegate userBusinessDelegate;

	@Value("${security.oauth2.authorizationserver.jwt.enabled:true}")
	private boolean jwtEnabled;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.withClientDetails(clientRepository::findByClientId);

	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		// @formatter:off
		endpoints.authenticationManager(this.authenticationManager).tokenStore(tokenStore());

		if (this.jwtEnabled) {
			endpoints.accessTokenConverter(accessTokenConverter());
		}
		// @formatter:on
		
		endpoints.userDetailsService(userBusinessDelegate);
	}

	@Bean
	public TokenStore tokenStore() {
		if (this.jwtEnabled) {
			return new JwtTokenStore(accessTokenConverter());
		} else {
			return new InMemoryTokenStore();
		}
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(this.keyPair);

		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		accessTokenConverter.setUserTokenConverter(new AuthTokenConverter());
		converter.setAccessTokenConverter(accessTokenConverter);

		return converter;
	}
}

