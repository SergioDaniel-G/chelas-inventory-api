package com.micheladas.chelas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CONTROLLER RESPONSIBLE FOR HANDLING SECURITY-RELATED EXCEPTIONS AND
 * DISPLAYING CUSTOM ERROR PAGES FOR UNAUTHORIZED ACCESS (ACCESS DENIED).
 */

@Controller
public class CustomErrorController {

    @GetMapping("/error/403")
    public String Forbidden() {

        return "error/403";
    }
}
