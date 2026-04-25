package com.micheladas.chelas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables global JPA Auditing to automatically track and persist entity
 * lifecycle metadata, such as creation and modification timestamps or users.
 */

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {
}
