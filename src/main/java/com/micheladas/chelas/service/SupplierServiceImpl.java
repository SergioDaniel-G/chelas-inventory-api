package com.micheladas.chelas.service;

import java.util.List;

import com.micheladas.chelas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.micheladas.chelas.entity.Supplier;
import com.micheladas.chelas.repository.SupplierRepository;

@Service
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	private SupplierRepository supplierRepository;

	@Override
	public List<Supplier> findAllProveedores() {
		return supplierRepository.findAll();
	}

	@Override
	public Page<Supplier> findAll(Pageable pageable) {
		return supplierRepository.findAll(pageable);
	}

	@Override
	public Supplier saveProveedores(Supplier supplier) {
		return supplierRepository.save(supplier);
	}

	@Override
	public Supplier getProveedoresById(Long id) {
		return supplierRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Proveedor", "ID", id));
	}

	@Override
	public Supplier updateProveedores(Supplier supplier) {
		return supplierRepository.save(supplier);
	}

	@Override
	public void deleteProveedores(Long id) {
		supplierRepository.deleteById(id);

	}

	@Override
	public List<Supplier> findBykeyword(String keyword) {
		return supplierRepository.findBykeyword(keyword);
	}

}
