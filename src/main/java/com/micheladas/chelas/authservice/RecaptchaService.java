package com.micheladas.chelas.authservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

/**
 * SERVICE TO VALIDATE GOOGLE reCAPTCHA V2/V3 TOKENS.
 * IT COMMUNICATES WITH GOOGLE 'S VERIFICATION API TO DISTINGUISH
 * HUMAN USERS FROM AUTOMATED BOTS DURING FORM SUBMISSIONS
 */

@Service
public class RecaptchaService {

    @Value("${google.recaptcha.secret}")
    private String secretKey;

    @Value("${google.recaptcha.url}")
    private String verifyUrl;

    private final RestTemplate restTemplate;

    public RecaptchaService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean validate(String responseToken) {
        if (responseToken == null || responseToken.isEmpty()) {
            return false;
        }

        String url = String.format("%s?secret=%s&response=%s", verifyUrl, secretKey, responseToken);

        try {

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);

            return response != null && Boolean.TRUE.equals(response.get("success"));
        } catch (Exception e) {

            return false;
        }
    }
}
