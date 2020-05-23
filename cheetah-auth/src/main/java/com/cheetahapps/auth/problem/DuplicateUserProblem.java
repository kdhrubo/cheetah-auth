package com.cheetahapps.auth.problem;

import java.net.URI;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import lombok.Getter;

public class DuplicateUserProblem extends AbstractThrowableProblem {
 
   
	private static final long serialVersionUID = 1L;
	private static final URI TYPE
      = URI.create("https://nirvaanacrm.com/users/duplicate");
	@Getter
	private final String errorCode = "USER-001";
 
    public DuplicateUserProblem(String email) {
        super(
          TYPE,
          "Duplicate User",
          Status.CONFLICT,
          String.format("User with email '%s' already exists.", email));
    }
 
}