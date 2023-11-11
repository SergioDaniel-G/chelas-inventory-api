package com.micheladas.chelas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micheladas.chelas.entidad.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

	public Usuario findByEmail(String email);

	public Usuario findByEmailAndMobileNumber(String email, String mobileNum);

}
