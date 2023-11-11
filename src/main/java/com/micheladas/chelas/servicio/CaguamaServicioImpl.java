package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.micheladas.chelas.entidad.Caguama;
import com.micheladas.chelas.repositorio.CaguamaRepositorio;

@Service
public class CaguamaServicioImpl implements CaguamaServicio {

	@Autowired
	private CaguamaRepositorio repositorio;
	
	@Override
	public List<Caguama> listarTodasLasCaguamas() {
		return repositorio.findAll();
	}

	@Override
	public Caguama guardarCaguamas(Caguama caguama) {
		return repositorio.save(caguama);
	}

	@Override
	public Caguama obtenerCaguamasPorId(Long id) {
		return repositorio.findById(id).get();
	}

	@Override
	public Caguama actualizarCaguamas(Caguama caguama) {
		return repositorio.save(caguama);
	}

	@Override
	public void eliminarCaguamas(Long id) {
		repositorio.deleteById(id);
		
	}

	@Override
	public Page<Caguama> findAll(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

}
