package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.micheladas.chelas.entidad.Precio;

public interface PrecioServicio {

	public List<Precio> listarTodosLosPrecios();// 1

	public Page<Precio> findAll(Pageable pageable); // 2

	public Precio guardarPrecios(Precio precio); // 3

	public Precio obtenerPreciosPorId(Long id);// 4

	public Precio actualizarPrecios(Precio precio);// 5

	public void eliminarPrecios(Long id);// 6

	public List<Precio> findBykeyword(String keyword);

}
