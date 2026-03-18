package com.micheladas.chelas.controlador;

import com.micheladas.chelas.entidad.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.micheladas.chelas.controlador.DTO.UsuarioRegistroDto;
import com.micheladas.chelas.servicio.UsuarioServicio;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/registro")
public class RegistroUsuarioControlador {

	private UsuarioServicio usuarioServicio;

	public RegistroUsuarioControlador(UsuarioServicio usuarioServicio) {
		super();
		this.usuarioServicio = usuarioServicio;
	}

	@ModelAttribute("usuario")
	public UsuarioRegistroDto retornarNuevoUsuarioRegistroDTO() {
		return new UsuarioRegistroDto();
	}

	@GetMapping
	public String mostrarFormularioDeRegistro() {
		return "auth/registro";
	}

	@PostMapping
	public String registrarCuentaDeUsuario(@Valid @ModelAttribute("usuario") UsuarioRegistroDto registroDTO,BindingResult result) {

		Usuario existente = usuarioServicio.findByEmail(registroDTO.getEmail());
		if (existente != null) {
			result.rejectValue("email", null, "Ya existe una cuenta registrada con este correo");
		}
		if (result.hasErrors()) {
			return "auth/registro"; // Regresa a la vista para mostrar los errores
		}
		usuarioServicio.guardar(registroDTO);
		return "redirect:/registro?exito";
	}

}