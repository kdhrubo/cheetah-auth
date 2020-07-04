package com.cheetahapps.auth.role;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleLoader implements ApplicationRunner {

	@Value("classpath:/data/roles.csv")
	private Resource resource;

	private final RoleBusinessDelegate roleBusinessDelegate;

	@Override
	public void run(ApplicationArguments args) {

		log.info("== check and load tenant ==");

		Try.run(() -> load());
	}

	

	public void load() throws IOException {

		try (Stream<String> stream = Files.lines(Paths.get(resource.getURI()))) {
			stream.forEach(i -> {

				roleBusinessDelegate.findByName(i).onEmpty(() -> {
					log.info("Creating system role - {}", i);
					Role role = Role.builder().name(i).system(true).build();

					this.roleBusinessDelegate.save(role);
				});

			});
		}
	}

}
