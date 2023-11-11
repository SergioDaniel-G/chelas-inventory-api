package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.micheladas.chelas.entidad.Precio;
import com.micheladas.chelas.repositorio.PrecioRepositorio;

@Service
public class PrecioServicioImpl implements PrecioServicio {

	@Autowired
	private PrecioRepositorio repositorio;

	@Override
	public List<Precio> listarTodosLosPrecios() {
		return repositorio.findAll();
	}

	@Override
	public Page<Precio> findAll(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	@Override
	public Precio guardarPrecios(Precio precio) {
		return repositorio.save(precio);
	}

	@Override
	public Precio obtenerPreciosPorId(Long id) {
		return repositorio.findById(id).get();
	}

	@Override
	public Precio actualizarPrecios(Precio precio) {
		return repositorio.save(precio);
	}

	@Override
	public void eliminarPrecios(Long id) {
		repositorio.deleteById(id);

	}

	@Override
	public List<Precio> findBykeyword(String keyword) {
		return repositorio.findBykeyword(keyword);
	}

}
