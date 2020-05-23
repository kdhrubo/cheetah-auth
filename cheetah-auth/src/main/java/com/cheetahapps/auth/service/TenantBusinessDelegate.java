package com.cheetahapps.auth.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Objects;

import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.cheetahapps.auth.domain.Tenant;
import com.cheetahapps.auth.domain.TenantSequence;
import com.cheetahapps.auth.event.BeforeUserRegisteredEvent;
import com.cheetahapps.auth.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TenantBusinessDelegate {

	private final TenantRepository tenantRepository;
	private final MongoOperations mongoOperations;

	@EventListener
	public void registerTenant(BeforeUserRegisteredEvent beforeUserRegisteredEvent) {
		String tenantName = beforeUserRegisteredEvent.getCompany();
		log.info("Registering new tenant - {}", tenantName);
		log.info("Checking if tenant exists");
		this.tenantRepository.findByName(tenantName).onEmpty(() -> {
			log.info("Tenant not found. New tenant to be created.");
			Tenant t = Tenant.builder().name(tenantName).code("T_" + getTenantSeq()).build();
			this.tenantRepository.save(t);

			updateBeforeUserRegisteredEvent(beforeUserRegisteredEvent, t, false);

		}).peek(t -> updateBeforeUserRegisteredEvent(beforeUserRegisteredEvent, t, true));

	}

	private void updateBeforeUserRegisteredEvent(BeforeUserRegisteredEvent beforeUserRegisteredEvent, Tenant t, boolean existingTenant) {
		beforeUserRegisteredEvent.setTenantId(t.getId());
		beforeUserRegisteredEvent.setTenantCode(t.getCode());
		beforeUserRegisteredEvent.setExistingTenant(existingTenant);
	}

	private long getTenantSeq() {
		TenantSequence counter = mongoOperations.findAndModify(query(where("_id").is("tenant_sequence")),
				new Update().inc("seq", 1), options().returnNew(true).upsert(true), TenantSequence.class);
		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}
}
