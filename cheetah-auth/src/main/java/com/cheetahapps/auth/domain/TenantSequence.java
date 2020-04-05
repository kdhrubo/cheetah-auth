package com.cheetahapps.auth.domain;



import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document("TenantSequence")
@TypeAlias("TenantSequence")
public class TenantSequence {
	
	@Id
    private String id;

    private long seq;

}
