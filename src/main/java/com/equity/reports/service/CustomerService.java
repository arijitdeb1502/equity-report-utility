package com.equity.reports.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equity.reports.dto.CustomerResponse;
import com.equity.reports.repository.CustomerRepository;

/**
 * Business logic for customer lookups.
 */
@Service
@Transactional(readOnly = true)
public class CustomerService {

	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	/** All customers, ordered by customer code. */
	public List<CustomerResponse> getAllCustomers() {
		return customerRepository.findAll(Sort.by("customerCode")).stream()
				.map(CustomerResponse::from)
				.toList();
	}
}
