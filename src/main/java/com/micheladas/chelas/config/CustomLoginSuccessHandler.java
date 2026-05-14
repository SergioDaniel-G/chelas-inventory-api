package com.micheladas.chelas.config;

import com.micheladas.chelas.authservice.IpService;
import com.micheladas.chelas.authservice.MfaEmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private IpService ipService;

    @Autowired
    private MfaEmailService mfaEmailService;

    @Autowired
    private com.micheladas.chelas.repository.UserIpRepository userIpRepository;

    @Autowired
    private com.micheladas.chelas.repository.UserRepository userRepository;

    private final HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName();
        String ip = request.getRemoteAddr();

        // SEARCH IN THE TABLE  IF EMAIL + IP + STATUS EXITOS
        boolean esDispositivoConocido = userIpRepository.existsByEmailAndIpAddressAndStatus(email, ip, "EXITOSO");

        if (esDispositivoConocido) {
            // CASE: I KNOW WHO YOU ARE , DIRECT PASS
            var userAccount = userRepository.findByEmail(email);

            // GIVE TO YOU REAL ROLES (ADMIN, ETC.)
            var realAuthorities = userAccount.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .toList();

            var fullAuth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    null,
                    realAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(fullAuth);
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

            ipService.registerAccessAttempt(email, "EXITOSO", "Acceso automático por IP conocida", request);
            getRedirectStrategy().sendRedirect(request, response, "/index");

        } else {
            // --- CASE: I DON´T KNOW YOU OR IS NEW IP
            ipService.registerAccessAttempt(email, "LOGIN_INICIAL", "Paso 1: Credenciales correctas", request);
            mfaEmailService.sendOtpEmail(email);

            var partialAuth = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_PRE_VERIFIED"))
            );

            SecurityContextHolder.getContext().setAuthentication(partialAuth);
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

            getRedirectStrategy().sendRedirect(request, response, "/verificar-codigo");
        }
    }
}