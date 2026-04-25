package com.micheladas.chelas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.micheladas.chelas.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

	@Query(value = "select * from loans l where l.id like %:keyword%" + " or l.customer_name like %:keyword%"
			+ " or l.quantity like %:keyword%" + " or l.order_description like %:keyword%"
			+ " or l.total_amount like %:keyword%", nativeQuery = true)
	List<Loan> findBykeyword(@Param("keyword") String keyword);
}
