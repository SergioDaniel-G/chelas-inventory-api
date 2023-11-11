package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.micheladas.chelas.entidad.Cliente;

public interface ClienteServicio {

	public List<Cliente> listarTodasLosClientes();//

	public Page<Cliente> findAll(Pageable pageable);// 2

	public Cliente guardarClientes(Cliente cliente);// 3

	public Cliente obtenerClientesPorId(Long id); // 4

	public Cliente actualizarClientes(Cliente cliente);// 5

	public void eliminarClientes(Long id);// 6

	public List<Cliente> findBykeyword(String keyword);

}
