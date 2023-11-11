package com.micheladas.chelas.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;//este pageable es de datadomain

import com.micheladas.chelas.entidad.Caguama;

public interface CaguamaServicio {
	

	//we create different methods to apply in the controller//
	
	public List<Caguama> listarTodasLasCaguamas();//1
	
	public Page<Caguama> findAll(Pageable pageable); //2

	public Caguama guardarCaguamas(Caguama caguama); //3

	public Caguama obtenerCaguamasPorId(Long id);//4

	public Caguama actualizarCaguamas(Caguama caguama);//5

	public void eliminarCaguamas(Long id);//6

}
