package com.micheladas.chelas.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.micheladas.chelas.entity.Loan;

public interface LoanService {

	public List<Loan> findAllLoans();

	public Page<Loan> findAll(Pageable pageable);

	public Loan saveLoans(Loan loan);

	public Loan getLoansById(Long id);

	public Loan updateLoans(Loan loan);

	public void deleteLoans(Long id);

	public List<Loan> findBykeyword(String keyword);

}
