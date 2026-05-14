package com.micheladas.chelas.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/* GLOBAL CONTROLLER INTERCEPTOR TO PROVIDE COMMON MODEL ATTRIBUTES
 * THIS ADVICE  SIMPLIFIES UI NAVIGATION BY AUTOMATICALLY CALCULATING
 * THE RETURN PATH (REFERER) FOR BACK BUTTONS ACROSS ALL VIEWS.
 */

@ControllerAdvice
public class BackNavigationAdvice {

    @ModelAttribute("backUrl")
    public String addBackUrl(HttpServletRequest request) {

        String referer = request.getHeader("Referer");

        if (referer == null || referer.contains(request.getRequestURI())) {
            return "/";
        }
        return referer;
    }
}
