package com.micheladas.chelas.service;

import java.util.List;
import com.micheladas.chelas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.micheladas.chelas.entity.Expense;
import com.micheladas.chelas.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepository;

	@Override
	public List<Expense> findAllExpenses() {

		return expenseRepository.findAll();
	}

	@Override
	public Page<Expense> findAll(Pageable pageable) {

		return expenseRepository.findAll(pageable);
	}

	@Override
	public Expense saveExpenses(Expense expense) {

		return expenseRepository.save(expense);
	}

	@Override
	public Expense getExpensesById(Long id) {

		return expenseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Gasto", "ID", id));
	}

	@Override
	public Expense updateExpenses(Expense expense) {

		return expenseRepository.save(expense);
	}

	@Override
	public void deleteExpenses(Long id) {
		expenseRepository.deleteById(id);

	}

	@Override
	public List<Expense> findBykeyword(String keyword) {

		return expenseRepository.findBykeyword(keyword);
	}

}
