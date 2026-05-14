package com.micheladas.chelas.config;

import com.micheladas.chelas.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * COMPONENT THAT REACTS TO SUCCESSFUL AUTHENTICATIONS
 * IT´S RESPONSIBLE FOR UPDATING USER METADATA SUCH AS THE LAST LOGIN DATE
 * AND RESETTING ANY PREVIOUS FAILED AUTHENTICATION ATTEMPTS.
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
