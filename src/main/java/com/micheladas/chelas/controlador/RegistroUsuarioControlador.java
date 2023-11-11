package com.micheladas.chelas.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.micheladas.chelas.controlador.DTO.UsuarioRegistroDto;
import com.micheladas.chelas.servicio.UsuarioServicio;

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
		return "registro";
	}

	@PostMapping
	public String registrarCuentaDeUsuario(@ModelAttribute("usuario") UsuarioRegistroDto registroDTO) {
		usuarioServicio.guardar(registroDTO);
		return "redirect:/registro?exito";
	}

}
