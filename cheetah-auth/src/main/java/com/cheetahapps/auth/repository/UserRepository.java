package com.cheetahapps.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cheetahapps.auth.domain.User;

import io.vavr.control.Option;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
	
	
    Option<User> findByEmail(String email);

}
