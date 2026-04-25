package com.micheladas.chelas.service;

import java.util.List;
import com.micheladas.chelas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.micheladas.chelas.entity.Loan;
import com.micheladas.chelas.repository.LoanRepository;

@Service
public class LoanServiceImpl implements LoanService {

	@Autowired
	private LoanRepository loanRepository;

	@Override
	public List<Loan> findAllLoans() {

		return loanRepository.findAll();
	}

	@Override
	public Page<Loan> findAll(Pageable pageable) {

		return loanRepository.findAll(pageable);
	}

	@Override
	public Loan saveLoans(Loan loan) {

		return loanRepository.save(loan);
	}

	@Override
	public Loan getLoansById(Long id) {
		return loanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Prestado", "ID", id));
	}

	@Override
	public Loan updateLoans(Loan loan) {

		return loanRepository.save(loan);
	}

	@Override
	public void deleteLoans(Long id) {
		loanRepository.deleteById(id);

	}

	@Override
	public List<Loan> findBykeyword(String keyword) {

		return loanRepository.findBykeyword(keyword);
	}

}
