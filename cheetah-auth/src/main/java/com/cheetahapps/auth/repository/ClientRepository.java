package com.cheetahapps.auth.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cheetahapps.auth.domain.Client;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {
	
    Client findByClientId(@Param("clientId") String clientId);
}
