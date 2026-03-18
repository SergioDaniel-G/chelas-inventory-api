package com.micheladas.chelas.servicio;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.micheladas.chelas.entidad.Rol;
import com.micheladas.chelas.repositorio.UsuarioRepositorio;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.micheladas.chelas.controlador.DTO.UsuarioRegistroDto;
import com.micheladas.chelas.entidad.Usuario;


@Service
public class UsuarioServicioImpl implements UsuarioServicio {

	private final UsuarioRepositorio usuarioRepositorio;
	private final BCryptPasswordEncoder passwordEncoder;

	// Constructor Injection para evitar @Autowired y facilitar testing
	public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio, BCryptPasswordEncoder passwordEncoder) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepositorio.findByEmail(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario o password inválidos");
		}
		return new User(usuario.getEmail(), usuario.getPassword(), mapearAutoridadesRoles(usuario.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Rol> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
	}

	@Override
	public Usuario guardar(UsuarioRegistroDto registroDTO) {
		// Usamos el "Constructor Inteligente" (Builder)
		Usuario usuario = Usuario.builder()
				.nombre(registroDTO.getNombre())
				.apellido(registroDTO.getApellido())
				.email(registroDTO.getEmail())
				.password(passwordEncoder.encode(registroDTO.getPassword()))
				.mobileNumber(registroDTO.getMobileNumber())
				.roles(Arrays.asList(new Rol("ROLE_USER")))
				.build(); // Esto junta todo y crea el usuario correctamente

		return usuarioRepositorio.save(usuario);
	}

	@Override
	public List<Usuario> listarUsuarios() {
		return usuarioRepositorio.findAll();
	}

	@Override
	public Usuario findByEmail(String email) {
		return usuarioRepositorio.findByEmail(email);
	}

}
