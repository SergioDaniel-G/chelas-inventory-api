package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.micheladas.chelas.entidad.Cigarro;

public interface CigarroServicio {

	public List<Cigarro> listarTodasLosCigarros();

	public Page<Cigarro> findAll(Pageable pageable);// 2

	public Cigarro guardarCigarros(Cigarro cigarro);// 3

	public Cigarro obtenerCigarrosPorId(Long id);// 4

	public Cigarro actualizarCigarros(Cigarro cigarro);// 5

	public void eliminarCigarros(Long id);// 6

	public List<Cigarro> findBykeyword(String keyword);

}
