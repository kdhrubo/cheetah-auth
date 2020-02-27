package com.cheetahapps.auth.config;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

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
		log.info(" Checking JKS ");
		RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
		RSAKey key = new RSAKey.Builder(publicKey).build();
		return new JWKSet(key).toJSONObject();
	}
}