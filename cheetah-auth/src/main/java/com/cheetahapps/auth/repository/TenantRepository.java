package com.cheetahapps.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.cheetahapps.auth.domain.Tenant;
import io.vavr.control.Option;

public interface TenantRepository extends MongoRepository<Tenant, String> {
	
	
    Option<Tenant> findByName(String email);

}
