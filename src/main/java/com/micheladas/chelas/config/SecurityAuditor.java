package com.micheladas.chelas.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.Optional;

/*
 * CUSTOM IMPLEMENTATION OF AUDITOR AWARE TO BRIDGE SPRING SECURITY´S CONTEXT
 * WITH JPA AUDITING, PROVIDING THE CURRENT AUTHENTICATED USER´S IDENTITY.
 */

@Component("auditorProvider")
public class SecurityAuditor implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("SYSTEM");
        }
        return Optional.of(authentication.getName());
    }
}
