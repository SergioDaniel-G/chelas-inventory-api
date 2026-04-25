package com.micheladas.chelas.service;

import java.util.List;
import com.micheladas.chelas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.micheladas.chelas.entity.Customer;
import com.micheladas.chelas.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Customer saveCustomers(Customer customer) {

		return customerRepository.save(customer);
	}

	@Override
	public Customer getCustomersById(Long id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente", "ID", id));
	}

	@Override
	public Customer updateCustomers(Customer customer) {

		return customerRepository.save(customer);
	}

	@Override
	public void deleteCustomers(Long id) {
		customerRepository.deleteById(id);

	}

	@Override
	public Page<Customer> findAll(Pageable pageable) {

		return customerRepository.findAll(pageable);
	}

	@Override
	public List<Customer> findAllCustomers() {

		return customerRepository.findAll();
	}

	@Override
	public List<Customer> findBykeyword(String keyword) {
		return customerRepository.findBykeyword(keyword);
	}

}
