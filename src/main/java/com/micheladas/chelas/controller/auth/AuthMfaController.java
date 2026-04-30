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

        if (mfaService.verifyCode(auth.getName(), code)) {

            ipService.registerIp(auth.getName(), request.getRemoteAddr());

            // SEARCH REAL USER IN THE DB FOR RECUPERATE ORIGINALS ROLE
            UserAccount userAccount = userRepository.findByEmail(auth.getName());

            // MAP REAL USERS
            var realAuthorities = userAccount.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            // CREATE THE NEW TOKEN WITH ALL ITS IDENTITY
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    auth.getPrincipal(),
                    null, // THE CREDENTIAL DELETE AFTER FIRST LOGIN
                    realAuthorities
            );

            // PRSISTENCE IN THE CONTEXT AND SESSION
            var context = SecurityContextHolder.getContext();
            context.setAuthentication(newAuth);
            securityContextRepository.saveContext(context, request, response);

            return "redirect:/index";
        }
        return "redirect:/verificar-codigo?error";
    }
}
