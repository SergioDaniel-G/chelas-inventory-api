package com.micheladas.chelas.controller;

import com.micheladas.chelas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {

	@Autowired
	private UserService userService;

	@GetMapping("/index")
	public String Index(Model model) {
		model.addAttribute("usuario", userService.UserLists());
		return "main/index";
	}

	/**
	 * Toggles the account lock status (Enabled/Disabled) for a specific user.
	 */

	@GetMapping("/usuarios/bloquear/{id}")
	public String toggleUser(@PathVariable Long id) {
		userService.toggleLockStatus(id);
		return "redirect:/index";
	}

	/* * NOTE: The manual logout mapping is commented out because Spring Security's
	 * LogoutFilter (defined in SecurityConfiguration) intercepts "/logout"
	 * before it ever reaches this controller.
	 *
	 * @GetMapping("/logout")
	 * public String logout(HttpServletRequest request) {
	 * request.getSession().invalidate();
	 * return "redirect:/login?logout";
	 * }
	 */

}
