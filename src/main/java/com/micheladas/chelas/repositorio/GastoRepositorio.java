package com.micheladas.chelas.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.micheladas.chelas.entidad.Gasto;

@Repository
public interface GastoRepositorio extends JpaRepository<Gasto, Long> {

	@Query("SELECT g FROM Gasto g WHERE " +
			"CAST(g.id AS string) LIKE %:keyword% OR " +
			"g.articulo LIKE %:keyword% OR " +
			"CAST(g.precio AS string) LIKE %:keyword% OR " +
			"CAST(g.total AS string) LIKE %:keyword%")
	List<Gasto> findBykeyword(@Param("keyword") String keyword);

}
