package com.cheetahapps.auth.config;

import javax.crypto.KeyGenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

@Configuration
public class OtpConfiguration {
	
	@Bean
	public TimeBasedOneTimePasswordGenerator totp() throws Exception{
		return new TimeBasedOneTimePasswordGenerator();
	}
	
	@Bean
	public KeyGenerator keyGenerator() throws Exception{
		KeyGenerator kg = KeyGenerator.getInstance(totp().getAlgorithm());
		
		// SHA-1 and SHA-256 prefer 64-byte (512-bit) keys; SHA512 prefers 128-byte (1024-bit) keys
	    kg.init(512);
	    
	    return kg;
	}

}
