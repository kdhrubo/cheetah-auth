package com.cheetahapps.auth.problem;

import org.springframework.core.NestedRuntimeException;

public class BusinessProcessingException extends NestedRuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public BusinessProcessingException(String msg) {
		super(msg);
		
	}

	public BusinessProcessingException(String msg, Throwable cause) {
		super(msg, cause);
		
	}

}
