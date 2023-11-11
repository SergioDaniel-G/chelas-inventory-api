package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.micheladas.chelas.entidad.Cliente;
import com.micheladas.chelas.repositorio.ClienteRepositorio;

@Service
public class ClienteServicioImpl implements ClienteServicio {

	@Autowired
	private ClienteRepositorio repositorio;

	@Override
	public Cliente guardarClientes(Cliente cliente) {
		return repositorio.save(cliente);
	}

	@Override
	public Cliente obtenerClientesPorId(Long id) {
		return repositorio.findById(id).get();
	}

	@Override
	public Cliente actualizarClientes(Cliente cliente) {
		return repositorio.save(cliente);
	}

	@Override
	public void eliminarClientes(Long id) {
		repositorio.deleteById(id);

	}

	@Override
	public Page<Cliente> findAll(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	@Override
	public List<Cliente> listarTodasLosClientes() {
		return repositorio.findAll();
	}

	@Override
	public List<Cliente> findBykeyword(String keyword) {
		return repositorio.findBykeyword(keyword);
	}

}
