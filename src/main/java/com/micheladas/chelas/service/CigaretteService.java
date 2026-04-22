package com.micheladas.chelas.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.micheladas.chelas.entity.Cigarette;

public interface CigaretteService {

	public List<Cigarette> findAllCigarettes();

	public Page<Cigarette> findAll(Pageable pageable);

	public Cigarette saveCigarettes(Cigarette cigarette);

	public Cigarette getCigarettesById(Long id);

	public Cigarette updateCigarettes(Cigarette cigarette);

	public void deleteCigarettes(Long id);

	public List<Cigarette> findBykeyword(String keyword);

}
