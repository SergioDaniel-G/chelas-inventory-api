package com.micheladas.chelas.controller.auth;

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

	@GetMapping("/login")
	public String login(@RequestParam(value = "timeout", required = false)String timeout,
						@RequestParam(value = "expired", required = false) String expired,
			Model model) {
		model.addAttribute("usuario", new UserRegistrationDto());

		if (expired != null || timeout != null) {
			model.addAttribute("msgInfo", "auth.session.expired");
		}

		return "auth/login";
	}

	/*
	 * DISPLAYS THE INITIAL PASSWORD RECOVERY
	 * SCREEN WHERE THE USER REQUEST A RESET
	 */

	@GetMapping("/loadForgotPassword")
	public String loadForgotPassword(@RequestParam(required = false) String msg, Model model) {
		if (msg != null) {
			model.addAttribute("msg", msg);
		}
		return "auth/forgot_password";
	}

	/* RENDERS THE FORM TO DEFINE A NEW PASSWORD AFTER
	 * SUCCESSFUL IDENTITY VERIFICATION
	 */

	@GetMapping("/loadResetPassword/{id}")
	public String loadResetPassword(@PathVariable Long id, Model model) {
		model.addAttribute("id", id);
		return "auth/reset_password";
	}

	/**
	 * VERIFIES USER CREDENTIALS TO AUTHORIZE A PASSWORD RESET.
	 */

	@PostMapping("/forgotPassword")
	public String forgotPassword(@RequestParam String email,
								 @RequestParam String mobileNum,
								 RedirectAttributes redirectAttributes) {

		UserAccount userAccount = userRepository.findByEmailAndMobileNumber(email, mobileNum);

		if (userAccount != null) {
			return "redirect:/loadResetPassword/" + userAccount.getId();
		} else {
            redirectAttributes.addFlashAttribute("errorMsg", "auth.reset.invalid");
			return "redirect:/loadForgotPassword";
		}
	}

	/* ENCRYPTS AND UPDATES THE USER´S PASSWORD IN
	 * THE DATABASE AFTER CONFIRMING MATCHING INPUTS
	 */

	@PostMapping("/changePassword")
	public String resetPassword(@RequestParam String password,
								@RequestParam String cpassword,
								@RequestParam Long id,
								RedirectAttributes redirectAttributes) {

		if (!password.equals(cpassword)) {
			redirectAttributes.addFlashAttribute("msg", "auth.reset.mismatch");
			return "redirect:/loadResetPassword/" + id;
		}

		UserAccount userAccount = userRepository.findById(id).orElse(null);

		if (userAccount != null) {
			String encryptPsw = passwordEncoder.encode(password);
			userAccount.setPassword(encryptPsw);
			userRepository.save(userAccount);
			redirectAttributes.addAttribute("msg", "auth.reset.success");
		} else {
			redirectAttributes.addAttribute("msg", "auth.reset.notfound");
		}

		return "redirect:/loadForgotPassword";
	}
}