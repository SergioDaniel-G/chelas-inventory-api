package com.micheladas.chelas.config;

import com.micheladas.chelas.entity.UserIp;
import com.micheladas.chelas.repository.UserIpRepository;
import com.micheladas.chelas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * COMPONENTS THAT LISTENS FOR SPRING SECURITY AUTHENTICATION EVENTS
 * IMPLEMENTS A SECURITY POLICY AGAINST BRUTE-FORCE ATTACKS BY
 * MANAGING THE FAILED ATTEMPTS COUNTER AND ACCOUNT LOCKING.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountLockoutListener {

    private final UserRepository userRepository;
    private final UserIpRepository userIpRepository;
    private static final int MAX_FAILED_ATTEMPTS = 5;

    @EventListener
    @Transactional
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
    @Transactional
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        String email = event.getAuthentication().getName();

        // 1. OBTAIN THE IP ADDRESS AND USER AGENT FROM THE REQUESTER
        jakarta.servlet.http.HttpServletRequest request =
                ((org.springframework.web.context.request.ServletRequestAttributes)
                        org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes()).getRequest();

        String ip = request.getRemoteAddr();
        String agent = request.getHeader("User-Agent");

        Optional.ofNullable(userRepository.findByEmail(email))
                .ifPresent(u -> {
                    if (!u.isAccountNonLocked()) {
                        log.error("Intento de acceso a cuenta YA bloqueada: {}", email);
                        return;
                    }

                    int newAttempts = u.getFailedAttempts() + 1;
                    u.setFailedAttempts(newAttempts);

                    // SAVE IN THE USER_IPS
                    UserIp logIp = new UserIp();
                    logIp.setEmail(email);
                    logIp.setIpAddress(ip);
                    logIp.setUserAgent(agent);
                    logIp.setStatus("FALLIDO_PASS");
                    logIp.setFailureReason("Pass incorrecta. Intento #" + newAttempts);
                    logIp.setRiskLevel(newAttempts >= 3 ? "MEDIUM" : "LOW");
                    logIp.setDevice(agent.toLowerCase().contains("mobi") ? "Móvil" : "PC");
                    logIp.setLoginTime(java.time.LocalDateTime.now());

                    userIpRepository.save(logIp);

                    log.warn("Intento fallido #{} para: {}", newAttempts, email);

                    if (newAttempts >= MAX_FAILED_ATTEMPTS) {
                        u.setAccountNonLocked(false);
                        log.error("¡BLOQUEO! Límite de intentos: {}", email);
                    }

                    userRepository.save(u);
                });
    }
}