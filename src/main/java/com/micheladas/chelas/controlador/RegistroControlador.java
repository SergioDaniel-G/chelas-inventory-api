package com.micheladas.chelas.controlador;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.micheladas.chelas.entidad.Usuario;
import com.micheladas.chelas.repositorio.UsuarioRepositorio;

@Controller
public class RegistroControlador {

	@Autowired
	private UsuarioRepositorio repositorio;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/login")
	public String iniciarSesion() {
		return "login";
	}

	@GetMapping("/loadForgotPassword")
	public String loadForgotPassword() {
		return "forgot_password";
	}

	@GetMapping("/loadResetPassword/{id}")
	public String loadResetPassword(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("id", id);
		return "reset_password";
	}

	@PostMapping("/forgotPassword")
	public String forgotPasword(@RequestParam String email, @RequestParam String mobileNum, HttpSession session) {
		Usuario user = repositorio.findByEmailAndMobileNumber(email, mobileNum);
		if (user != null) {
			return "redirect:/loadResetPassword/" + user.getId();
		} else {
			session.setAttribute("msg", "correo y/o número de teléfono inválido");
			return "forgot_password";
		}
	}

	@PostMapping("/changePassword")
	public String resetPassword(@RequestParam String psw, @RequestParam Long id, HttpSession session) {
		Usuario user = repositorio.findById(id).get();
		String encryptPsw = passwordEncoder.encode(psw);
		user.setPassword(encryptPsw);
		Usuario updateUser = repositorio.save(user);
		if (updateUser != null) {
			session.setAttribute("msg", "password change sucessfully");
		}
		return "redirect:/loadForgotPassword";

	}
}
