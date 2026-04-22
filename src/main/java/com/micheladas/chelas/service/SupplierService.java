package com.micheladas.chelas.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.micheladas.chelas.entity.Supplier;

public interface SupplierService {

	public List<Supplier> findAllProveedores();

	public Page<Supplier> findAll(Pageable pageable);

	public Supplier saveProveedores(Supplier supplier);

	public Supplier getProveedoresById(Long id);

	public Supplier updateProveedores(Supplier supplier);

	public void deleteProveedores(Long id);

	public List<Supplier> findBykeyword(String keyword);

}
