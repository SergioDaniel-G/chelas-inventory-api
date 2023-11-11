package com.micheladas.chelas.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.micheladas.chelas.entidad.Proveedor;

@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, Long> {

	@Query(value = "select * from proveedores p where p.id like %:keyword%" + " or p.nombre like %:keyword%"
			+ " or p.telefono like %:keyword%" + " or p.ubicacion like %:keyword%", nativeQuery = true)
	List<Proveedor> findBykeyword(@Param("keyword") String keyword);
}
