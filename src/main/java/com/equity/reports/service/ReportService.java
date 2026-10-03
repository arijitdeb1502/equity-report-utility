package com.equity.reports.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equity.reports.repository.CustomerRepository;
import com.equity.reports.repository.TradeRepository;

/**
 * Builds reports that combine customer and trade data.
 */
@Service
@Transactional(readOnly = true)
public class ReportService {

	private final CustomerRepository customerRepository;
	private final TradeRepository tradeRepository;

	public ReportService(CustomerRepository customerRepository, TradeRepository tradeRepository) {
		this.customerRepository = customerRepository;
		this.tradeRepository = tradeRepository;
	}
}
