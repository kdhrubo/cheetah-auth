package com.cheetahapps.auth.domain;

import java.util.Arrays;

public enum GrantType {

    AUTHORIZATION_CODE("authorization_code"),
    PASSWORD("password"),
    REFRESH_TOKEN("refresh_token");

    private String code;

    GrantType(String code) {
        this.code = code;
    }

    public static boolean contains(GrantType grantType) {
        return Arrays.stream(GrantType.values())
                .anyMatch(type -> grantType.getCode().equals(type.getCode()));
    }

    public String getCode() {
        return this.code;
    }
    
   
}
