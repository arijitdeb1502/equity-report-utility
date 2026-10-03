package com.equity.reports.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equity.reports.dto.CustomerResponse;
import com.equity.reports.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers", description = "Customer lookups")
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping
	@Operation(summary = "Get all customers",
			description = "Returns every customer ordered by customer code. PAN and demat numbers are masked.")
	@ApiResponse(responseCode = "200", description = "List of customers (empty if there are none)")
	public List<CustomerResponse> getAllCustomers() {
		return customerService.getAllCustomers();
	}
}
