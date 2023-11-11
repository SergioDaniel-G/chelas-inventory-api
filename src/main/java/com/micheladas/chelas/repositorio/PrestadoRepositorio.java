package com.micheladas.chelas.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.micheladas.chelas.entidad.Prestado;

@Repository
public interface PrestadoRepositorio extends JpaRepository<Prestado, Long> {

	@Query(value = "select * from prestados p where p.id like %:keyword%" + " or p.nombre like %:keyword%"
			+ " or p.cantidad like %:keyword%" + " or p.descripcion_del_pedido like %:keyword%"
			+ " or p.total like %:keyword%", nativeQuery = true)
	List<Prestado> findBykeyword(@Param("keyword") String keyword);
}
