package com.micheladas.chelas.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.micheladas.chelas.entity.Customer;

public interface CustomerService {

	public List<Customer> findAllCustomers();

	public Page<Customer> findAll(Pageable pageable);

	public Customer saveCustomers(Customer customer);

	public Customer getCustomersById(Long id);

	public Customer updateCustomers(Customer customer);

	public void deleteCustomers(Long id);

	public List<Customer> findBykeyword(String keyword);

}
