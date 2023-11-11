package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.micheladas.chelas.entidad.Proveedor;

public interface ProveedorServicio {

	// we create different methods to apply in the controller//

	public List<Proveedor> listarTodasLasProveedores();// 1

	public Page<Proveedor> findAll(Pageable pageable); // 2

	public Proveedor guardarProveedores(Proveedor proveedor); // 3

	public Proveedor obtenerProveedoresPorId(Long id);// 4

	public Proveedor actualizarProveedores(Proveedor proveedor);// 5

	public void eliminarProveedores(Long id);// 6

	public List<Proveedor> findBykeyword(String keyword);

}
