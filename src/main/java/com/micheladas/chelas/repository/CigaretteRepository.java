package com.micheladas.chelas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.micheladas.chelas.entity.Cigarette;

public interface CigaretteRepository extends JpaRepository<Cigarette, Long> {

	@Query(value = "select * from cigarettes ci where ci.brand like %:keyword%"
			+ " or ci.unit_price like %:keyword%" + " or ci.total_amount like %:keyword%"
			+ " or ci.quantity like %:keyword%", nativeQuery = true)
	List<Cigarette> findByKeyword(@Param("keyword") String keyword);
}
