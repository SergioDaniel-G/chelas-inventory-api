package com.micheladas.chelas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.micheladas.chelas.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

	@Query(value = "select * from suppliers s where s.id like %:keyword%" + " or s.name like %:keyword%"
			+ " or s.phone like %:keyword%" + " or s.location like %:keyword%", nativeQuery = true)
	List<Supplier> findBykeyword(@Param("keyword") String keyword);
}
