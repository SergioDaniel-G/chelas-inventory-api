package com.micheladas.chelas.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.micheladas.chelas.entity.BigBottle;

public interface BigBottleService {
	
	public List<BigBottle> findAllBigBottle();
	
	public Page<BigBottle> findAll(Pageable pageable);

	public BigBottle saveBigBottle(BigBottle bigBottle);

	public BigBottle getBigBottleById(Long id);

	public BigBottle updateBigBottle(BigBottle bigBottle);

	public void deleteBigBottle(Long id);

}
