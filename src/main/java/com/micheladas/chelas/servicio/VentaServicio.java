package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.micheladas.chelas.entidad.Venta;

public interface VentaServicio {

	// we create different methods to apply in the controller//

	public List<Venta> listarTodasLasVentas();// 1

	public Page<Venta> findAll(Pageable pageable); // 2

	public Venta guardarVentas(Venta venta); // 3

	public Venta obtenerVentasPorId(Long id);// 4

	public Venta actualizarVentas(Venta venta);// 5

	public void eliminarVentas(Long id);// 6

	public List<Venta> findBykeyword(String keyword);

}
