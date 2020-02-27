package com.cheetahapps.auth;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.cheetahapps.auth.domain.Role;
import com.cheetahapps.auth.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleLoader implements ApplicationRunner {

	private final RoleRepository roleRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		Role role = roleRepository.findByName(Role.USER);
		if (role == null) {
			log.info("Saving role");
			roleRepository.save(Role.builder().name(Role.USER).build());
		}
	}

}
