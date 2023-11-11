package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.micheladas.chelas.entidad.Prestado;

public interface PrestadoServicio {

	// we create different methods to apply in the controller//

	public List<Prestado> listarTodasLasPrestados();// 1

	public Page<Prestado> findAll(Pageable pageable); // 2

	public Prestado guardarPrestados(Prestado prestado); // 3

	public Prestado obtenerPrestadosPorId(Long id);// 4

	public Prestado actualizarPrestados(Prestado prestado);// 5

	public void eliminarPrestados(Long id);// 6

	public List<Prestado> findBykeyword(String keyword);

}
