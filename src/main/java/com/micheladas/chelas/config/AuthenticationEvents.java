package com.micheladas.chelas.config;

import com.micheladas.chelas.entity.UserAccount;
import com.micheladas.chelas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Event listener component that manages authentication success and failure events
 * to implement brute-force protection and account locking policies.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationEvents {

    private final UserRepository userRepository;
    private static final int MAX_FAILED_ATTEMPTS = 5;

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

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        String email = event.getAuthentication().getName();

        Optional.ofNullable(userRepository.findByEmail(email))
                .filter(UserAccount::isAccountNonLocked)
                .ifPresent(u -> {
                    int newAttempts = u.getFailedAttempts() + 1;
                    u.setFailedAttempts(newAttempts);

                    log.warn("Intento fallido #{} para: {}", newAttempts, email);

                    if (newAttempts >= MAX_FAILED_ATTEMPTS) {
                        u.setAccountNonLocked(false);
                        log.error("¡CUENTA BLOQUEADA!: {}", email);
                    }
                    userRepository.save(u);
                });
    }
}