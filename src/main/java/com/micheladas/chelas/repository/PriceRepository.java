package com.micheladas.chelas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.micheladas.chelas.entity.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {

	@Query(value = "select * from prices p where p.id like %:keyword%" + " or p.product_id like %:keyword%"
			+ " or p.product_price like %:keyword%" + " or p.description like %:keyword%"
			+ " or p.stock like %:keyword%", nativeQuery = true)
	List<Price> findBykeyword(@Param("keyword") String keyword);

}
