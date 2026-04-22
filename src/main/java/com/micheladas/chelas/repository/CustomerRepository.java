package com.micheladas.chelas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.micheladas.chelas.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query(value = "select * from customers c where c.full_name like %:keyword%"
			+ " or c.customer_number like %:keyword%" + " or c.address like %:keyword%"
			+ " or c.zip_code like %:keyword%", nativeQuery = true)
	List<Customer> findBykeyword(@Param("keyword") String keyword);

}
