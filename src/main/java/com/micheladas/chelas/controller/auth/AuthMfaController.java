package com.micheladas.chelas.controller.auth;

import com.micheladas.chelas.authservice.IpService;
import com.micheladas.chelas.authservice.MfaEmailService;
import com.micheladas.chelas.entity.UserAccount;
import com.micheladas.chelas.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.stream.Collectors;

@Controller
public class AuthMfaController {

    private final MfaEmailService mfaService;
    private final UserRepository userRepository;
    private final IpService ipService;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public AuthMfaController(MfaEmailService mfaService, UserRepository userRepository, IpService ipService) {
        this.mfaService = mfaService;
        this.userRepository = userRepository;
        this.ipService = ipService;
    }

    @GetMapping("/verificar-codigo")
    public String showMfaPage() {

        return "auth/mfa-page";
    }

    @PostMapping("/auth/validar-otp")
    public String validateMfa(@RequestParam String code, Authentication auth,
                              HttpServletRequest request, HttpServletResponse response) {

        // OBTAIN THE USER FROM DATABASE
        UserAccount userAccount = userRepository.findByEmail(auth.getName());

        // CHECK FOR LIMIT REACHED
        if (userAccount.getOtpFailedAttempts() >= 5) {
            // LOG ATTEMPT WHILE BLOCKED (MEDIUM-HIGH RISK)
            ipService.registerAccessAttempt(auth.getName(), "FALLIDO", "Usuario bloqueado por intentos", request);
            return "redirect:/verificar-codigo?error=bloqueado";
        }

        // TRY TO VERIFY THE CODE
        if (mfaService.verifyCode(auth.getName(), code)) {

            // SUCCESS
            userAccount.setOtpFailedAttempts(0);
            userRepository.save(userAccount);

            // DETAILED LOG OF SUCCESSFUL ACCESS (DETECT HUMAN VS BOT)
            ipService.registerAccessAttempt(auth.getName(), "EXITOSO", null, request);

            var realAuthorities = userAccount.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    auth.getPrincipal(),
                    null,
                    realAuthorities
            );

            var context = SecurityContextHolder.getContext();
            context.setAuthentication(newAuth);
            securityContextRepository.saveContext(context, request, response);

            return "redirect:/index";
        } else {
            // ERROR
            int nuevosIntentos = userAccount.getOtpFailedAttempts() + 1;
            userAccount.setOtpFailedAttempts(nuevosIntentos);
            userRepository.save(userAccount);

            // DETAILED FAILURE LOG FOR AUDIT PURPOSE
            String motivo = "Código OTP incorrecto. Intento #" + nuevosIntentos;
            ipService.registerAccessAttempt(auth.getName(), "FALLIDO", motivo, request);

            if (nuevosIntentos >= 5) {
                return "redirect:/verificar-codigo?error=bloqueado";
            }

            return "redirect:/verificar-codigo?error&restan=" + (5 - nuevosIntentos);
        }
    }
}

