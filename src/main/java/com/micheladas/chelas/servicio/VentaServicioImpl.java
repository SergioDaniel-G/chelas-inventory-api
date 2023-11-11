package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.micheladas.chelas.entidad.Venta;
import com.micheladas.chelas.repositorio.VentaRepositorio;

@Service
public class VentaServicioImpl implements VentaServicio {

	@Autowired
	private VentaRepositorio repositorio;

	@Override
	public List<Venta> listarTodasLasVentas() {
		return repositorio.findAll();
	}

	@Override
	public Page<Venta> findAll(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	@Override
	public Venta guardarVentas(Venta venta) {
		return repositorio.save(venta);
	}

	@Override
	public Venta obtenerVentasPorId(Long id) {
		return repositorio.findById(id).get();
	}

	@Override
	public Venta actualizarVentas(Venta venta) {
		return repositorio.save(venta);
	}

	@Override
	public void eliminarVentas(Long id) {
		repositorio.deleteById(id);

	}

	@Override
	public List<Venta> findBykeyword(String keyword) {
		return repositorio.findBykeyword(keyword);
	}

}
