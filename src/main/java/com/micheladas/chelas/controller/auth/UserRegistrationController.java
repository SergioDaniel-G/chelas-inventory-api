package com.micheladas.chelas.controller.auth;

import com.micheladas.chelas.authservice.RecaptchaService;
import com.micheladas.chelas.entity.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

	private final UserService userService;
	private final RecaptchaService recaptchaService;
	private final Environment env;

	public UserRegistrationController(UserService userService,
									  RecaptchaService recaptchaService,
									  Environment env) {
		this.userService = userService;
		this.recaptchaService = recaptchaService;
		this.env = env;
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
									  @RequestParam(name = "g-recaptcha-response", required = false) String captchaResponse, // 1. Cambiado a required = false
									  Model model) {

		// 2. Obtener el estado de RECAPTCHA_ENABLED desde el entorno
		String isRecaptchaEnabled = env.getProperty("RECAPTCHA_ENABLED");

		// 3. Solo validar si el captcha está habilitado (si es null o diferente de "false")
		if (isRecaptchaEnabled == null || !isRecaptchaEnabled.equals("false")) {
			if (captchaResponse == null || !recaptchaService.validate(captchaResponse)) {
				model.addAttribute("error", "Verificación de seguridad fallida. Inténtalo de nuevo.");
				return "auth/registro";
			}
		}

		UserAccount accountExist = userService.findByEmail(userRegistrationDto.getEmail());
		if (accountExist != null) {
			result.rejectValue("email", null, "Este correo ya esta registrado");
		}

		if (result.hasErrors()) {
			return "auth/registro";
		}

		userService.save(userRegistrationDto);
		return "redirect:/registro?exito";
	}

}