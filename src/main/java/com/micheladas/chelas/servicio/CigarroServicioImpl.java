package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.micheladas.chelas.entidad.Cigarro;
import com.micheladas.chelas.repositorio.CigarroRepositorio;

@Service
public class CigarroServicioImpl implements CigarroServicio {

	@Autowired
	private CigarroRepositorio repositorio;

	@Override
	public List<Cigarro> listarTodasLosCigarros() {
		return repositorio.findAll();
	}

	@Override
	public Cigarro guardarCigarros(Cigarro cigarro) {
		return repositorio.save(cigarro);
	}

	@Override
	public Cigarro obtenerCigarrosPorId(Long id) {
		return repositorio.findById(id).get();
	}

	@Override
	public Cigarro actualizarCigarros(Cigarro cigarro) {
		return repositorio.save(cigarro);
	}

	@Override
	public void eliminarCigarros(Long id) {
		repositorio.deleteById(id);

	}

	@Override
	public Page<Cigarro> findAll(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	@Override
	public List<Cigarro> findBykeyword(String keyword) {
		return repositorio.findBykeyword(keyword);
	}

}
