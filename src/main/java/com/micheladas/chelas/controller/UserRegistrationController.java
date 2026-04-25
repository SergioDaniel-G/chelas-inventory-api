package com.micheladas.chelas.controller;

import com.micheladas.chelas.config.RecaptchaService;
import com.micheladas.chelas.entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.micheladas.chelas.controller.DTO.UserRegistrationDto;
import com.micheladas.chelas.service.UserService;
import jakarta.validation.Valid;

/**
 * Controller handling the user self-registration workflow, ensuring data
 * validation and unique identity constraints before account creation.
 */
@Controller
@RequestMapping("/registro")
public class UserRegistrationController {

	@Autowired
	private RecaptchaService recaptchaService;

	private UserService userService;

	public UserRegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}

	@ModelAttribute("usuario")
	public UserRegistrationDto returnNewUserRegistrationDto() {

		return new UserRegistrationDto();
	}

	@GetMapping
	public String showRegisterForm() {

		return "auth/registro";
	}

	/**
	 * Processes the registration request, validating the reCAPTCHA,
	 * checking for email uniqueness, and persisting the new user account.
	 */

	@PostMapping
	public String userAccountRegister(@Valid @ModelAttribute("usuario") UserRegistrationDto userRegistrationDto,
									  BindingResult result,
									  @RequestParam(name = "g-recaptcha-response") String captchaResponse,
									  Model model) {

		if (!recaptchaService.validate(captchaResponse)) {
			model.addAttribute("error", "Por favor, verifica el reCAPTCHA .");
			return "auth/registro";
		}

		UserAccount accountExist = userService.findByEmail(userRegistrationDto.getEmail());

		if (accountExist != null) {
			result.rejectValue("email", null, "Ya existe una cuenta registrada con este correo");
		}
		if (result.hasErrors()) {
			return "auth/registro";
		}
		userService.save(userRegistrationDto);
		return "redirect:/registro?exito";
	}

}