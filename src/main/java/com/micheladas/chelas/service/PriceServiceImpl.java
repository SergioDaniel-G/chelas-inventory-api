package com.micheladas.chelas.service;

import java.util.List;

import com.micheladas.chelas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.micheladas.chelas.entity.Price;
import com.micheladas.chelas.repository.PriceRepository;

@Service
public class PriceServiceImpl implements PriceService {

	@Autowired
	private PriceRepository priceRepository;

	@Override
	public List<Price> findAllPrices() {
		return priceRepository.findAll();
	}

	@Override
	public Page<Price> findAll(Pageable pageable) {
		return priceRepository.findAll(pageable);
	}

	@Override
	public Price savePrices(Price price) {
		return priceRepository.save(price);
	}

	@Override
	public Price getPricesById(Long id) {

		return priceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Precio", "ID", id));
	}

	@Override
	public Price updatePrices(Price price) {
		return priceRepository.save(price);
	}

	@Override
	public void deletePrices(Long id) {
		priceRepository.deleteById(id);

	}

	@Override
	public List<Price> findBykeyword(String keyword) {
		return priceRepository.findBykeyword(keyword);
	}

}
