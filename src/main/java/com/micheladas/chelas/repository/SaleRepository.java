package com.micheladas.chelas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.micheladas.chelas.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

	@Query(value = "select * from sales s where s.cigarettes_quantity like %:keyword%" + " or s.cups_quantity like %:keyword%"
			+ " or s.bigbottle_quantity like %:keyword%" + " or s.bigbottle_stock like %:keyword%" + " or s.id like %:keyword%"
			+ " or s.cigarettes_stock like %:keyword%" + " or s.total_amount like %:keyword%", nativeQuery = true)
	List<Sale> findBykeyword(@Param("keyword") String keyword);

}
