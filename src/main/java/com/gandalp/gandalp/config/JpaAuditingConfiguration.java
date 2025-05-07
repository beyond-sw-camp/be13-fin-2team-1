package com.gandalp.gandalp.config;

import com.gandalp.gandalp.common.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {


	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}
}
