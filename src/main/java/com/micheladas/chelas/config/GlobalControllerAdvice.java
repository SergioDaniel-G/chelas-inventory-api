package com.micheladas.chelas.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("backUrl") // 3. Esto crea la variable que usarás en Thymeleaf
    public String addBackUrl(HttpServletRequest request) {
        String referer = request.getHeader("Referer");

        // Si no hay página anterior, mandamos al inicio
        if (referer == null || referer.contains(request.getRequestURI())) {
            return "/";
        }
        return referer;
    }
}
