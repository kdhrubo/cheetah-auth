package com.cheetahapps.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document("User")
@TypeAlias("User")
public class User implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	@Indexed(unique = true)
	private String email; 
	private String mobile;
	
	
	private String password;
	private String firstName;
	private String lastName;
	
	private String role;
	private String roleId;
	
	private boolean deleted;
	
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	
	@CreatedDate
	private LocalDateTime createdDate;

	private LocalDateTime lastLoginDate;
	
	private String verificationCode;
	
	private LocalDateTime verificationCodeCreatedDate;
	
	
	private String tenantId;
	private String tenantName;
	private String tenantCode;
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return createAuthorityList(role);
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.mobile;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !this.deleted;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !this.deleted;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !this.deleted;
	}

	@Override
	public boolean isEnabled() {
		return !this.deleted;
	}

}
