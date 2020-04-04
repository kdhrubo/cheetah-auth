package com.cheetahapps.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cheetahapps.auth.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * For configuring the end users recognized by this Authorization Server
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	
	private final UserService userService;
	private final BCryptPasswordEncoder passwordEncoder;

	private static final String[] AUTHENTICATION_WHITELIST = { 
			"/introspect", 
			"/health",
			"/users/otp/**",
			"/users/register/**", 
			"/users/changepassword/**"};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        

		http.authorizeRequests()
		
		.mvcMatchers("/.well-known/jwks.json").permitAll().and().authorizeRequests()
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
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }


	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return userService;
	}
}