package com.cheetahapps.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cheetahapps.auth.domain.Otp;

import io.vavr.control.Option;

@Repository
public interface OtpRepository extends MongoRepository<Otp, Integer> {

	Option<Otp> findByMobileNo(String mobileNo);
}
