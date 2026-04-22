package com.micheladas.chelas.service;

import java.util.List;

import com.micheladas.chelas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.micheladas.chelas.entity.Sale;
import com.micheladas.chelas.repository.SaleRepository;

@Service
public class SaleServiceImpl implements SaleService {

	@Autowired
	private SaleRepository saleRepository;

	@Override
	public List<Sale> findAllSales() {
		return saleRepository.findAll();
	}

	@Override
	public Page<Sale> findAll(Pageable pageable) {
		return saleRepository.findAll(pageable);
	}

	@Override
	public Sale saveSales(Sale sale) {
		return saleRepository.save(sale);
	}

	@Override
	public Sale getSalesById(Long id) {
		return saleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Venta", "ID", id));
	}

	@Override
	public Sale updateSales(Sale sale) {
		return saleRepository.save(sale);
	}

	@Override
	public void deleteSales(Long id) {
		saleRepository.deleteById(id);

	}

	@Override
	public List<Sale> findBykeyword(String keyword) {
		return saleRepository.findBykeyword(keyword);
	}

}
