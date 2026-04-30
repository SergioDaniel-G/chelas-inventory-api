package com.micheladas.chelas.authservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MfaEmailService {

    @Autowired
    private JavaMailSender mailSender;

    // TEMPORARY MEMORY FOR THE CODE
    private Map<String, String> otpCache = new ConcurrentHashMap<>();

    public void sendOtpEmail(String email) {
        // GENERATE SIX DIGITS CODE
        String code = String.valueOf((int)(Math.random() * 900000) + 100000);

        // SAVE TO CACHE
        otpCache.put(email, code);

        // PRINT TO CONSOLO FOR TEST
        System.out.println("---------- DEBUG MFA ----------");
        System.out.println("Email: " + email + " | Código: " + code);
        System.out.println("-------------------------------");

        // CONFIGUES AND SEND RHE EMAIL
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Código de Seguridad - Micheladas App");
            message.setText("Tu código de acceso es: " + code + "\n\nEste código es de un solo uso.");

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("CRÍTICO: No se pudo enviar el correo a " + email + ". Error: " + e.getMessage());
        }
    }

    public boolean verifyCode(String email, String code) {
        String validCode = otpCache.get(email);

        // IF THE CODE MATCHES
        if (validCode != null && validCode.equals(code)) {
            // REMOVE IT FORM CACHE INMEDIATELY
            otpCache.remove(email);
            return true;
        }
        // IF I DOESN´T MATCH, DON´T REMOVE IT IN CASE THE USER MADE A TYPO AND WANTS TO RETRY
        return false;
    }
}
