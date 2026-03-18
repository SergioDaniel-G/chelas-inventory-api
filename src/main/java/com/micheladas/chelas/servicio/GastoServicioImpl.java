package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.micheladas.chelas.entidad.Gasto;
import com.micheladas.chelas.repositorio.GastoRepositorio;

@Service
public class GastoServicioImpl implements GastoServicio {

	@Autowired
	private GastoRepositorio repositorio;

	@Override
	public List<Gasto> listarTodosLosGastos() {
		return repositorio.findAll();
	}

	@Override
	public Page<Gasto> findAll(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	@Override
	public Gasto guardarGastos(Gasto gasto) {
		return repositorio.save(gasto);
	}

	@Override
	public Gasto obtenerGastosPorId(Long id) {
		return repositorio.findById(id).orElse(null);
	}

	@Override
	public Gasto actualizarGastos(Gasto gasto) {
		return repositorio.save(gasto);
	}

	@Override
	public void eliminarGastos(Long id) {
		repositorio.deleteById(id);

	}

	@Override
	public List<Gasto> findBykeyword(String keyword) {
		return repositorio.findBykeyword(keyword);
	}

}
