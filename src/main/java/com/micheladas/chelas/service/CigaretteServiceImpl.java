package com.micheladas.chelas.service;

import java.util.List;

import com.micheladas.chelas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.micheladas.chelas.entity.Cigarette;
import com.micheladas.chelas.repository.CigaretteRepository;

@Service
public class CigaretteServiceImpl implements CigaretteService {

	@Autowired
	private CigaretteRepository cigaretteRepository;

	@Override
	public List<Cigarette> findAllCigarettes() {
		return cigaretteRepository.findAll();
	}

	@Override
	public Cigarette saveCigarettes(Cigarette cigarette) {

		return cigaretteRepository.save(cigarette);
	}

	@Override
	public Cigarette getCigarettesById(Long id) {

		return cigaretteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cigarro", "ID", id));
	}

	@Override
	public Cigarette updateCigarettes(Cigarette cigarette) {

		return cigaretteRepository.save(cigarette);
	}

	@Override
	public void deleteCigarettes(Long id) {
		cigaretteRepository.deleteById(id);

	}

	@Override
	public Page<Cigarette> findAll(Pageable pageable) {
		return cigaretteRepository.findAll(pageable);
	}

	@Override
	public List<Cigarette> findBykeyword(String keyword) {
		return cigaretteRepository.findByKeyword(keyword);
	}

}
