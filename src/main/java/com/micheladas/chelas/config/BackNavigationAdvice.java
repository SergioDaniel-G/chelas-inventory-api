package com.micheladas.chelas.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**Global controller interceptor to provide common model attributes.
 * This advice simplifies UI navigation by automatically calculating
 * the return path (referer) for "Back" buttons across all views.
 **/

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
