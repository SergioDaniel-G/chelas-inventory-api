package com.micheladas.chelas.config;

import com.micheladas.chelas.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Component that reacts to successful authentications.
 * It is responsible for updating user metadata, such as the last login date,
 * and resetting any previous failed authentication attempts.
 */

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserService userService;

    public AuthenticationSuccessListener(UserService userService) {

        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {

        Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();

            userService.updateLastLoginDate(email, LocalDateTime.now());
        }
    }
}
