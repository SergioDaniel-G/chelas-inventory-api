package com.micheladas.chelas.controlador;

import com.micheladas.chelas.controlador.DTO.UsuarioRegistroDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.micheladas.chelas.entidad.Usuario;
import com.micheladas.chelas.repositorio.UsuarioRepositorio;

@Controller
public class RegistroControlador {

	@Autowired
	private UsuarioRepositorio repositorio;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/login")
	public String iniciarSesion(Model modelo) {
		modelo.addAttribute("usuario", new UsuarioRegistroDto());
		return "auth/login";
	}

	@GetMapping("/loadForgotPassword")
	public String loadForgotPassword(@RequestParam(required = false) String msg, Model modelo) {
		if (msg != null) {
			modelo.addAttribute("msg", msg);
		}
		return "auth/forgot_password";
	}

	@GetMapping("/loadResetPassword/{id}")
	public String loadResetPassword(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("id", id);
		return "auth/reset_password";
	}

	@PostMapping("/forgotPassword")
	public String forgotPasword(@RequestParam String email,
								@RequestParam String mobileNum,
								RedirectAttributes redirectAttributes) {

		Usuario user = repositorio.findByEmailAndMobileNumber(email, mobileNum);

		if (user != null) {
			return "redirect:/loadResetPassword/" + user.getId();
		} else {
			redirectAttributes.addAttribute("msg", "Correo y/o número de teléfono inválido");
			return "redirect:/loadForgotPassword";
		}
	}

	@PostMapping("/changePassword")
	public String resetPassword(@RequestParam String psw,
								@RequestParam String cpsw,
								@RequestParam Long id,
								RedirectAttributes redirectAttributes) {

		// 2. Validación de coincidencia
		if (!psw.equals(cpsw)) {
			redirectAttributes.addFlashAttribute("msg", "Las contraseñas no coinciden");
			return "redirect:/loadResetPassword/" + id; // Regresamos al formulario
		}

		Usuario user = repositorio.findById(id).orElse(null);

		if (user != null) {
			String encryptPsw = passwordEncoder.encode(psw);
			user.setPassword(encryptPsw);
			repositorio.save(user);
			redirectAttributes.addAttribute("msg", "Password cambiado exitosamente");
		} else {
			redirectAttributes.addAttribute("msg", "Usuario no encontrado");
		}

		return "redirect:/loadForgotPassword";
	}
}