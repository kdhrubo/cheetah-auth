package com.cheetahapps.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cheetahapps.auth.service.UserBusinessDelegate;

import lombok.RequiredArgsConstructor;

/**
 * For configuring the end users recognized by this Authorization Server
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	
	private final UserBusinessDelegate userBusinessDelegate;
	private final BCryptPasswordEncoder passwordEncoder;

	private static final String[] AUTHENTICATION_WHITELIST = { 
			"/introspect", 
			"/health",
			"/users/otp/**",
			"/users/register/**", 
			"/users/changepassword/**",
			"/issuer/.well-known/openid-configuration", 
			"/.well-known/openid-configuration/issuer",
			"/.well-known/oauth-authorization-server/issuer",
			"/.well-known/jwks.json"
	
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        

		http.authorizeRequests()
		
		.mvcMatchers(AUTHENTICATION_WHITELIST).permitAll().and().authorizeRequests()
				.antMatchers(AUTHENTICATION_WHITELIST).permitAll().anyRequest().authenticated().and().httpBasic().and().csrf()
				.ignoringAntMatchers(AUTHENTICATION_WHITELIST);
	}

	@Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userBusinessDelegate)
                .passwordEncoder(passwordEncoder);
    }


	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return userBusinessDelegate;
	}
}