package com.cheetahapps.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cheetahapps.auth.domain.Role;

public interface RoleRepository extends MongoRepository<Role, Integer> {

	Role findByName(String name);
}
