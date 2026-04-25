package com.micheladas.chelas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.micheladas.chelas.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

	@Query(value = "select * from expenses e where e.id like %:keyword%" + " or e.item_name like %:keyword%"
			+ " or e.quantity like %:keyword%" + " or e.unit_price like %:keyword%"
			+ " or e.total_amount like %:keyword%", nativeQuery = true)
	List<Expense> findBykeyword(@Param("keyword") String keyword);

}
