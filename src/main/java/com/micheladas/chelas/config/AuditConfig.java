package com.micheladas.chelas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * ENABLES GLOBAL JPA AUDITING TO AUTOMATICALLY TRACK AND PERSIST ENTITY
 * LIFECYCLE METADATA, SUCH AS CREATION AND MODIFICATION TIMESTAMP OR USERS.
 */

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {
}
