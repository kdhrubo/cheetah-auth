package com.cheetahapps.auth.service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheetahapps.auth.domain.User;
import com.cheetahapps.auth.event.BeforeUserRegisteredEvent;
import com.cheetahapps.auth.integration.AwsEmailSender;
import com.cheetahapps.auth.integration.SlackMessageSender;
import com.cheetahapps.auth.problem.BusinessProcessingException;
import com.cheetahapps.auth.problem.DuplicateUserProblem;
import com.cheetahapps.auth.repository.UserRepository;
import com.cheetahapps.auth.role.Role;
//import com.cheetahapps.auth.role.RoleRepository;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserBusinessDelegate implements UserDetailsService {

	private final UserRepository userRepository;
	//private final RoleRepository roleRepository;
	private final ApplicationEventPublisher eventPublisher;

	private final PasswordEncoder passwordEncoder;
	private final SlackMessageSender slackMessageSender;

	private final TimeBasedOneTimePasswordGenerator totp;
	private final KeyGenerator keyGenerator;

	private final AwsEmailSender awsEmailSender;

	

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) {
		log.info("#  Finding user with mobile - {}", username);

		UserDetails userDetails = null;

		Option<User> user = findByEmail(username.trim());

		if (!user.isEmpty()) {

			log.info("User found");

			User u = user.get();

			userDetails = u;
		} else {
			throw new UsernameNotFoundException("User not found with mobile - " + username);
		}

		return userDetails;
	}

	@Transactional(readOnly = true)
	public Option<User> findByEmail(String email) {
		log.info("Finding user with email - {}", email);
		return userRepository.findByEmail(email);
	}
	
	@Deprecated
	@Transactional
	public User register(User user, String company, String country) {
		//User with given email can exist for only one tenant. 
		
		findByEmail(user.getEmail()).onEmpty(() -> registerInternal(user, company, country))
		.peek(u ->  { throw new DuplicateUserProblem(user.getEmail());});

		
		slackMessageSender
				.send("New user created - " + user.getEmail() + " , Name - " + user.getFirstName() + " " + user.getLastName());

		log.info("New tenant provisioning to start");


		return user;
	}
	
	@Deprecated
	protected User registerInternal(User user, String company, String country) {
		BeforeUserRegisteredEvent beforeUserRegisteredEvent = BeforeUserRegisteredEvent.builder().company(company)
				.country(country).build();

		this.eventPublisher.publishEvent(beforeUserRegisteredEvent);

		log.info("Registering user - {}", user.getEmail());

		
		Role role = null;
		if (beforeUserRegisteredEvent.isExistingTenant()) {// admin needs to activate this user
			//role = roleRepository.findByName(Role.USER);
			user.setDeleted(true); 
		} else {// this user is auto active
			//role = roleRepository.findByName(Role.COMPANY_ADMIN);
		}
		
		user.setRoleId(role.getId());
		user.setRole(role.getName());
		
		return userRepository.findByEmail(user.getEmail()).getOrElse(() -> {
			user.setTenantCode(beforeUserRegisteredEvent.getTenantCode());
			user.setTenantId(beforeUserRegisteredEvent.getTenantId());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			return this.userRepository.save(user);
		});
		
	}
	
	
	public User generateOtp(String email) throws Exception {

		Option<User> user = this.userRepository.findByEmail(email);

		if (user.isEmpty()) {
			throw new BusinessProcessingException("User with given email does not exist");

		}

		Key key = keyGenerator.generateKey();
		Instant now = Instant.now();
		int pwd = totp.generateOneTimePassword(key, now);

		User u = user.get();
		u.setVerificationCode(pwd + "");
		u.setVerificationCodeCreatedDate(LocalDateTime.now());

		Map<String, String> data = new HashMap<>();
		data.put("otp", pwd + "");

		awsEmailSender.send("dhrubo.kayal@gmail.com", "BongReads - Forgot Password? Verification Code", "welcome",
				data);

		return this.userRepository.save(u);
	}

	public User updatePassword(String email, String verificationCode, String password) {
		Option<User> user = this.userRepository.findByEmail(email);

		if (user.isEmpty()) {
			throw new BusinessProcessingException("User with given email does not exist");

		}

		User u = user.get();

		if (!u.getVerificationCode().equals(verificationCode)) {
			throw new BusinessProcessingException("Failed to match verificaiton code.");
		}

		u.setPassword(passwordEncoder.encode(password));

		return this.userRepository.save(u);
	}

}
