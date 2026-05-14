package com.micheladas.chelas.authservice;

import com.micheladas.chelas.entity.UserIp;
import com.micheladas.chelas.repository.UserIpRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IpService {

    private final UserIpRepository userIpRepository;

    public IpService(UserIpRepository userIpRepository) {
        this.userIpRepository = userIpRepository;
    }

    public boolean isIpKnown(String email, String ip) {

        return userIpRepository.existsByEmailAndIpAddressAndStatus(email, ip, "EXITOSO");
    }

    /**
     * RECORD EACH ACCESS ATTEMPT.
     * DOES NOT UPDATE,ALWAYS INSERT ANEW ROW TO MAINTAIN A COMPLETE HISTORY
     */

    @Transactional
    public void registerAccessAttempt(String email, String status, String reason, HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();

        // BOTS DETECTION
        boolean isBot = detectBot(userAgent);

        // DETERMINE DEVICE
        String device = (userAgent != null && userAgent.contains("Mobi")) ? "Móvil" : "PC";

        // CALCULATE RISK LEVEL
        String risk = "LOW";
        if (isBot) {
            risk = "HIGH";
        } else if ("FALLIDO".equals(status)) {
            risk = "MEDIUM";
        }

        // BUILT AND SAVE THE REGISTER
        UserIp log = UserIp.builder()
                .email(email)
                .ipAddress(ipAddress)
                .device(device)
                .userAgent(userAgent)
                .isBot(isBot)
                .riskLevel(risk)
                .status(status)
                .failureReason(reason)
                .build();

        userIpRepository.save(log);
    }

    private boolean detectBot(String userAgent) {
        if (userAgent == null) return true;
        String uaLower = userAgent.toLowerCase();
        return uaLower.contains("bot") ||
                uaLower.contains("python") ||
                uaLower.contains("curl") ||
                uaLower.contains("postman") ||
                uaLower.contains("httpclient");
    }
}