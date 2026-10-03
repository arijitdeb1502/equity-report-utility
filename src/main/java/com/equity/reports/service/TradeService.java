package com.equity.reports.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equity.reports.repository.TradeRepository;

/**
 * Business logic for trade lookups.
 */
@Service
@Transactional(readOnly = true)
public class TradeService {

	private final TradeRepository tradeRepository;

	public TradeService(TradeRepository tradeRepository) {
		this.tradeRepository = tradeRepository;
	}
}
