package com.cheetahapps.auth.domain;

import java.util.Arrays;

public enum ScopeType {

    OPEN_ID("openid");

    private String code;

    ScopeType(String code) {
        this.code = code;
    }

    public static boolean contains(ScopeType scopeType) {
        return Arrays.stream(ScopeType.values())
                .anyMatch(type -> scopeType.getCode().equals(type.getCode()));
    }

    public String getCode() {
        return this.code;
    }
}
