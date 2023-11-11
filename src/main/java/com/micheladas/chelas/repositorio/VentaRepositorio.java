package com.micheladas.chelas.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.micheladas.chelas.entidad.Venta;

@Repository
public interface VentaRepositorio extends JpaRepository<Venta, Long> {

	@Query(value = "select * from ventas v where v.cigarros like %:keyword%" + " or v.vasos like %:keyword%"
			+ " or v.caguamas like %:keyword%" + " or v.stock_caguamas like %:keyword%" + " or v.id like %:keyword%"
			+ " or v.stock_cigarros like %:keyword%" + " or v.total like %:keyword%", nativeQuery = true)
	List<Venta> findBykeyword(@Param("keyword") String keyword);

}
