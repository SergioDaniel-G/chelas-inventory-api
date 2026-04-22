package com.micheladas.chelas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for handling security-related exceptions and
 * displaying custom error pages for unauthorized access.
 */
@Controller
public class CustomErrorController {

    @GetMapping("/error/403")
    public String Forbidden() {
        return "error/403";
    }
}
