package com.micheladas.chelas.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.micheladas.chelas.entity.Expense;

public interface ExpenseService {

	public List<Expense> findAllExpenses();

	public Page<Expense> findAll(Pageable pageable);

	public Expense saveExpenses(Expense expense);

	public Expense getExpensesById(Long id);

	public Expense updateExpenses(Expense expense);

	public void deleteExpenses(Long id);

	public List<Expense> findBykeyword(String keyword);

}
