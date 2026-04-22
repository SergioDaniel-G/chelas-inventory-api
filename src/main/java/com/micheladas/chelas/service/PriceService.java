package com.micheladas.chelas.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.micheladas.chelas.entity.Price;

public interface PriceService {

	public List<Price> findAllPrices();

	public Page<Price> findAll(Pageable pageable);

	public Price savePrices(Price price);

	public Price getPricesById(Long id);

	public Price updatePrices(Price price);

	public void deletePrices(Long id);

	public List<Price> findBykeyword(String keyword);

}
