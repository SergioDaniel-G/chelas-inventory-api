package com.micheladas.chelas.authservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

/**
 * Service to validate Google reCAPTCHA v2/v3 tokens.
 * It communicates with Google's verification API to distinguish
 * human users from automated bots during form submissions.
 */

@Service
public class RecaptchaService {

    @Value("${RECAPTCHA_ENABLED:true}")
    private boolean isEnabled;

    @Value("${google.recaptcha.secret}")
    private String secretKey;

    @Value("${google.recaptcha.url}")
    private String verifyUrl;

    private final RestTemplate restTemplate;

    public RecaptchaService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean validate(String responseToken) {
        // --- BYPASS PARA RECLUTADORES / DESARROLLO ---
        if (!isEnabled) {
            System.out.println(">>> [MODO LOCAL]: reCAPTCHA deshabilitado. Validando automáticamente.");
            return true;
        }

        if (responseToken == null || responseToken.isEmpty()) {
            return false;
        }

        String url = String.format("%s?secret=%s&response=%s", verifyUrl, secretKey, responseToken);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);

            return response != null && Boolean.TRUE.equals(response.get("success"));
        } catch (Exception e) {
            // ERROR LOG
            System.err.println("Error al conectar con Google reCAPTCHA: " + e.getMessage());
            return false;
        }
    }
}
