package com.cheetahapps.auth.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cheetahapps.auth.domain.Role;


@Repository
public interface RoleRepository extends MongoRepository<Role, Integer> {

	
	Role findByName(@Param("name") String name);
}
