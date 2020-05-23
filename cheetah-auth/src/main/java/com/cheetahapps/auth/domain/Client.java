package com.cheetahapps.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import com.fasterxml.jackson.annotation.JsonInclude;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document("Client")
@TypeAlias("Client")
public class Client implements ClientDetails {

    
	private static final long serialVersionUID = 1L;

	@Id
    private String id;

    private String clientId;
    private String clientSecret;
    private String scopes;
    private String grantTypes;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return this.clientSecret;
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
    	return new HashSet<>(Arrays.asList(this.scopes.split(",")));
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
    	return new HashSet<>(Arrays.asList(this.grantTypes.split(",")));
        
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return createAuthorityList(this.scopes.split(","));
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return false;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return Collections.emptySet();
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return Collections.emptyMap();
    }

    @Override
    public Set<String> getResourceIds() {
        return Collections.emptySet();
    }
}
