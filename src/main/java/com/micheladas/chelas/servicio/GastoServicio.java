package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.micheladas.chelas.entidad.Gasto;

public interface GastoServicio {

	public List<Gasto> listarTodosLosGastos();// 1

	public Page<Gasto> findAll(Pageable pageable); // 2

	public Gasto guardarGastos(Gasto gasto); // 3

	public Gasto obtenerGastosPorId(Long id);// 4

	public Gasto actualizarGastos(Gasto gasto);// 5

	public void eliminarGastos(Long id);// 6

	public List<Gasto> findBykeyword(String keyword);

}
