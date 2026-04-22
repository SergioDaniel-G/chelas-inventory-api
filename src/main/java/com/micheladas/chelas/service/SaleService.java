package com.micheladas.chelas.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.micheladas.chelas.entity.Sale;

public interface SaleService {

	public List<Sale> findAllSales();

	public Page<Sale> findAll(Pageable pageable);

	public Sale saveSales(Sale sale);

	public Sale getSalesById(Long id);

	public Sale updateSales(Sale sale);

	public void deleteSales(Long id);

	public List<Sale> findBykeyword(String keyword);

}
