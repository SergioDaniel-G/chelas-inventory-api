package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.micheladas.chelas.controlador.DTO.UsuarioRegistroDto;
import com.micheladas.chelas.entidad.Usuario;

public interface UsuarioServicio extends UserDetailsService {

	public Usuario guardar(UsuarioRegistroDto registroDTO);

	public List<Usuario> listarUsuarios();

}
