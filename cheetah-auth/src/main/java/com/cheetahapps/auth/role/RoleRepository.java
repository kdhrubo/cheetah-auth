package com.cheetahapps.auth.role;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.vavr.control.Option;


interface RoleRepository extends MongoRepository<Role, String> {

	Option<Role> findByName(String name);
}
