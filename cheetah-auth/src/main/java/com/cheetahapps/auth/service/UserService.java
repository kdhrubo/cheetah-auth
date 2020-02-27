package com.cheetahapps.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cheetahapps.auth.domain.User;
import com.cheetahapps.auth.repository.UserRepository;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

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

}
