package com.micheladas.chelas.service;

import java.util.List;

import com.micheladas.chelas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.micheladas.chelas.entity.BigBottle;
import com.micheladas.chelas.repository.BigBottleRepository;

@Service
public class BigBottleServiceImpl implements BigBottleService {

	@Autowired
	private BigBottleRepository bigBottleRepository;
	
	@Override
	public List<BigBottle> findAllBigBottle() {

		return bigBottleRepository.findAll();
	}

	@Override
	public BigBottle saveBigBottle(BigBottle bigBottle) {

		return bigBottleRepository.save(bigBottle);
	}

	@Override
	public BigBottle getBigBottleById(Long id) {
		return bigBottleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Caguama", "ID", id));
	}

	@Override
	public BigBottle updateBigBottle(BigBottle bigBottle) {
		return bigBottleRepository.save(bigBottle);
	}

	@Override
	public void deleteBigBottle(Long id) {
		bigBottleRepository.deleteById(id);
		
	}

	@Override
	public Page<BigBottle> findAll(Pageable pageable) {

		return bigBottleRepository.findAll(pageable);
	}

}
