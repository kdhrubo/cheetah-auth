package com.cheetahapps.auth.config;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import io.vavr.collection.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Legacy Authorization Server (spring-security-oauth2) does not support any <a
 * href target="_blank" href="https://tools.ietf.org/html/rfc7517#section-5">JWK
 * Set</a> endpoint.
 *
 * This class adds ad-hoc support in order to better support the other samples
 * in the repo.
 */
@FrameworkEndpoint
@RequiredArgsConstructor
@Slf4j
public class JwkSetEndpoint {
	private final KeyPair keyPair;

	@GetMapping("/.well-known/jwks.json")
	@ResponseBody
	public Map<String, Object> getKey() {
		log.info(" ======= Getting JKS Key======== ");
		RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
		RSAKey key = new RSAKey.Builder(publicKey).build();
		return new JWKSet(key).toJSONObject();
	}
	
	//This method is called it is a hack, because of bug in spring resource server code.
	@GetMapping({"/issuer/.well-known/openid-configuration", "/issuer/.well-known/openid-configuration/issuer"
			, "/issuer/.well-known/oauth-authorization-server/issuer"})
	@ResponseBody
	public Map<String, String> getAuthzServerIssuer() {
		return HashMap.of(
				"jwks_uri", "http://localhost:7001/auth/.well-known/jwks.json",
				"issuer", "http://localhost:7001/auth/issuer"
				)
				
				.toJavaMap();
	}
	
	
}