package com.micheladas.chelas.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.micheladas.chelas.entidad.Gasto;

@Repository
public interface GastoRepositorio extends JpaRepository<Gasto, Long> {

	@Query(value = "select * from gastos g where g.id like %:keyword%" + " or g.articulo like %:keyword%"
			+ " or g.cantidad like %:keyword%" + " or g.total like %:keyword%"
			+ " or g.precio like %:keyword%", nativeQuery = true)
	List<Gasto> findBykeyword(@Param("keyword") String keyword);

}
