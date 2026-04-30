package com.micheladas.chelas.config;

import com.micheladas.chelas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Component that listens for Spring Security authentication events.
 * Implements a security policy against brute-force attacks by
 * managing the failed attempts counter and account locking.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountLockoutListener {

    private final UserRepository userRepository;
    private static final int MAX_FAILED_ATTEMPTS = 5;

    /**
     * Executes after a successful authentication.
     * If the user had accumulated failed attempts, they are reset to zero.
     */

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();

        Optional.ofNullable(userRepository.findByEmail(email))
                .filter(u -> u.getFailedAttempts() > 0)
                .ifPresent(u -> {
                    u.setFailedAttempts(0);
                    userRepository.save(u);
                    log.info("Login exitoso: Intentos reiniciados para {}", email);
                });
    }

    /**
     * Executes when an authentication error occurs (incorrect password, etc.).
     * Increments the failure counter and locks the account if the limit is reached.
     */

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        String email = event.getAuthentication().getName();

        Optional.ofNullable(userRepository.findByEmail(email))
                .ifPresent(u -> {

                    if (!u.isAccountNonLocked()) {
                        log.error("Intento de acceso a cuenta YA bloqueada: {}", email);
                        return;
                    }

                    int newAttempts = u.getFailedAttempts() + 1;
                    u.setFailedAttempts(newAttempts);

                    log.warn("Intento fallido #{} para: {}", newAttempts, email);

                    if (newAttempts >= MAX_FAILED_ATTEMPTS) {
                        u.setAccountNonLocked(false);
                        log.error("¡LIMITE DE INTENTOS ALCANZADO! Cuenta bloqueada: {}", email);
                    }

                    userRepository.save(u);
                });
    }
}