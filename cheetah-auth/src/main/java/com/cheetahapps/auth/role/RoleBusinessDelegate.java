package com.cheetahapps.auth.role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleBusinessDelegate {
	
	private final RoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	public Option<Role> findByName(String name){
		return roleRepository.findByName(name);
	}
	@Transactional
	public Role save(Role role) {
		return this.roleRepository.save(role);
	}
}
