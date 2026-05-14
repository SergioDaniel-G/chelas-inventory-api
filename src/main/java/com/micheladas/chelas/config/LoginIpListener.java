package com.micheladas.chelas.config;

import com.micheladas.chelas.authservice.IpService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class LoginIpListener {

    @Autowired
    private IpService ipService;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();

            // SET AS "LOGIN_INICIAL" TO DISTINGUISH FROM OTP VERIFICATION
            ipService.registerAccessAttempt(email, "LOGIN_INICIAL", "Paso 1: Credenciales correctas", request);

            System.out.println("DEBUG: Auditoría inicial registrada para " + email);
        }
    }
}
