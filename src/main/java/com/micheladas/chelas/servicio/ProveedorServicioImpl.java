package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.micheladas.chelas.entidad.Proveedor;
import com.micheladas.chelas.repositorio.ProveedorRepositorio;

@Service
public class ProveedorServicioImpl implements ProveedorServicio {

	@Autowired
	private ProveedorRepositorio repositorio;

	@Override
	public List<Proveedor> listarTodasLasProveedores() {
		return repositorio.findAll();
	}

	@Override
	public Page<Proveedor> findAll(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	@Override
	public Proveedor guardarProveedores(Proveedor proveedor) {
		return repositorio.save(proveedor);
	}

	@Override
	public Proveedor obtenerProveedoresPorId(Long id) {
		return repositorio.findById(id).get();
	}

	@Override
	public Proveedor actualizarProveedores(Proveedor proveedor) {
		return repositorio.save(proveedor);
	}

	@Override
	public void eliminarProveedores(Long id) {
		repositorio.deleteById(id);

	}

	@Override
	public List<Proveedor> findBykeyword(String keyword) {
		return repositorio.findBykeyword(keyword);
	}

}
