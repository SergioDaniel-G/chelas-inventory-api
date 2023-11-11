package com.micheladas.chelas.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.micheladas.chelas.entidad.Precio;

public interface PrecioRepositorio extends JpaRepository<Precio, Long> {

	@Query(value = "select * from precios p where p.id like %:keyword%" + " or p.id_producto like %:keyword%"
			+ " or p.precio_producto like %:keyword%" + " or p.descripcion like %:keyword%"
			+ " or p.existencia like %:keyword%", nativeQuery = true)
	List<Precio> findBykeyword(@Param("keyword") String keyword);

}
