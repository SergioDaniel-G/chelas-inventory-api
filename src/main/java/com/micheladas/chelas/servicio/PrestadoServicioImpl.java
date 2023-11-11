package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.micheladas.chelas.entidad.Prestado;
import com.micheladas.chelas.repositorio.PrestadoRepositorio;

@Service
public class PrestadoServicioImpl implements PrestadoServicio {

	@Autowired
	private PrestadoRepositorio repositorio;

	@Override
	public List<Prestado> listarTodasLasPrestados() {
		return repositorio.findAll();
	}

	@Override
	public Page<Prestado> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return repositorio.findAll(pageable);
	}

	@Override
	public Prestado guardarPrestados(Prestado prestado) {
		return repositorio.save(prestado);
	}

	@Override
	public Prestado obtenerPrestadosPorId(Long id) {
		return repositorio.findById(id).get();
	}

	@Override
	public Prestado actualizarPrestados(Prestado prestado) {
		return repositorio.save(prestado);
	}

	@Override
	public void eliminarPrestados(Long id) {
		repositorio.deleteById(id);

	}

	@Override
	public List<Prestado> findBykeyword(String keyword) {
		return repositorio.findBykeyword(keyword);
	}

}
