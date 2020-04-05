package com.cheetahapps.auth.service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheetahapps.auth.domain.Role;
import com.cheetahapps.auth.domain.Tenant;
import com.cheetahapps.auth.domain.TenantSequence;
import com.cheetahapps.auth.domain.User;
import com.cheetahapps.auth.integration.AwsEmailSender;
import com.cheetahapps.auth.integration.SlackMessageSender;
import com.cheetahapps.auth.repository.RoleRepository;
import com.cheetahapps.auth.repository.TenantRepository;
import com.cheetahapps.auth.repository.UserRepository;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Objects;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final TenantRepository tenantRepository;
	private final MongoOperations mongoOperations;
	private final PasswordEncoder passwordEncoder;
	private final SlackMessageSender slackMessageSender;

	private final TimeBasedOneTimePasswordGenerator totp;
	private final KeyGenerator keyGenerator;

	private final AwsEmailSender awsEmailSender;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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

	public User register(User user) {

		log.info("Check and save tenant first. - {}", user.getTenant().getName());

		Option<Tenant> t = this.tenantRepository.findByName(user.getTenant().getName());

		if (t.isEmpty()) {
			user.getTenant().setCode("T_" + getTenantSeq());
			Tenant tentant = this.tenantRepository.save(user.getTenant());
			
			user.setTenant(tentant);
		} else {
			user.setTenant(t.get());
		}

		log.info("Registering user - {}", user.getEmail());

		Role role = roleRepository.findByName(Role.COMPANY_ADMIN);

		user.setRole(role);

		String password = user.getPassword();
		user.setPassword(passwordEncoder.encode(password));

		User u = this.userRepository.save(user);

		slackMessageSender
				.send("New user created - " + u.getEmail() + " , Name - " + u.getFirstName() + " " + u.getLastName());

		return u;
	}

	public User generateOtp(String email) throws Exception {

		Option<User> user = this.userRepository.findByEmail(email);

		if (user.isEmpty()) {
			throw new RuntimeException("User with given email does not exist");

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
			throw new RuntimeException("User with given email does not exist");

		}

		User u = user.get();

		if (!u.getVerificationCode().equals(verificationCode)) {
			throw new RuntimeException("Failed to match verificaiton code.");
		}

		u.setPassword(passwordEncoder.encode(password));

		return this.userRepository.save(u);
	}

	private long getTenantSeq() {
		TenantSequence counter = mongoOperations.findAndModify(query(where("_id").is("tenant_sequence")),
				new Update().inc("seq", 1), options().returnNew(true).upsert(true), TenantSequence.class);
		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}

}
