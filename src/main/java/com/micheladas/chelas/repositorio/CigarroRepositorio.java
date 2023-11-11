package com.micheladas.chelas.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.micheladas.chelas.entidad.Cigarro;

public interface CigarroRepositorio extends JpaRepository<Cigarro, Long> {

	@Query(value = "select * from cigarros ci where ci.id like %:keyword%" + " or ci.marca like %:keyword%"
			+ " or ci.precio like %:keyword%" + " or ci.total like %:keyword%"
			+ " or ci.cantidad like %:keyword%", nativeQuery = true)
	List<Cigarro> findBykeyword(@Param("keyword") String keyword);
}
