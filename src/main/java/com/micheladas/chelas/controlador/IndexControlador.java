package com.micheladas.chelas.controlador;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexControlador {

	@GetMapping("/index")
	public String Index() {
		return "index";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		// Remove the user from the session
		request.getSession().removeAttribute("user");
		// Invalidate the session
		request.getSession().invalidate();
		// Redirect to the login page
		return "redirect:/login";
	}

}
