package com.micheladas.chelas.controller;

import com.micheladas.chelas.controller.DTO.UserRegistrationDto;
import com.micheladas.chelas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.micheladas.chelas.entity.UserAccount;
import com.micheladas.chelas.repository.UserRepository;

@Controller
public class RegistrationController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	/* Show login page */
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("usuario", new UserRegistrationDto());
		return "auth/login";
	}

	/* Loads the screen where the user requests to recover their password. */
	@GetMapping("/loadForgotPassword")
	public String loadForgotPassword(@RequestParam(required = false) String msg, Model model) {
		if (msg != null) {
			model.addAttribute("msg", msg);
		}
		return "auth/forgot_password";
	}

	/*Displays the form to enter the new password.*/
	@GetMapping("/loadResetPassword/{id}")
	public String loadResetPassword(@PathVariable Long id, Model model) {
		model.addAttribute("id", id);
		return "auth/reset_password";
	}

	/* User authentication for password recovery */
	@PostMapping("/forgotPassword")
	public String forgotPasword(@RequestParam String email,
								@RequestParam String mobileNum,
								RedirectAttributes redirectAttributes) {

		UserAccount userAccount = userRepository.findByEmailAndMobileNumber(email, mobileNum);

		if (userAccount != null) {
			return "redirect:/loadResetPassword/" + userAccount.getId();
		} else {
			redirectAttributes.addAttribute("msg", "Correo y/o número de teléfono inválido");
			return "redirect:/loadForgotPassword";
		}
	}

	/* The method that finally saves the new password */
	@PostMapping("/changePassword")
	public String resetPassword(@RequestParam String password,
								@RequestParam String cpassword,
								@RequestParam Long id,
								RedirectAttributes redirectAttributes) {

		if (!password.equals(cpassword)) {
			redirectAttributes.addFlashAttribute("msg", "Las contraseñas no coinciden");
			return "redirect:/loadResetPassword/" + id;
		}

		UserAccount userAccount = userRepository.findById(id).orElse(null);

		if (userAccount != null) {
			String encryptPsw = passwordEncoder.encode(password);
			userAccount.setPassword(encryptPsw);
			userRepository.save(userAccount);
			redirectAttributes.addAttribute("msg", "Contrasena cambiada correctamente");
		} else {
			redirectAttributes.addAttribute("msg", "Usuario no encontrado");
		}

		return "redirect:/loadForgotPassword";
	}
}