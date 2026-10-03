package com.equity.reports.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
