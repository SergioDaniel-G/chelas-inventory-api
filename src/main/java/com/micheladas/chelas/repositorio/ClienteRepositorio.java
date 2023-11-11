package com.micheladas.chelas.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.micheladas.chelas.entidad.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

	@Query(value = "select * from clientes c where c.nombre_completo like %:keyword%"
			+ " or c.num_cliente like %:keyword%" + " or c.direccion like %:keyword%"
			+ " or c.cp like %:keyword%", nativeQuery = true)
	List<Cliente> findBykeyword(@Param("keyword") String keyword);

}
