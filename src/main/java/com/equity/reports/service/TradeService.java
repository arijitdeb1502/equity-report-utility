package com.equity.reports.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equity.reports.dto.TradeResponse;
import com.equity.reports.repository.TradeRepository;

/**
 * Business logic for trade lookups.
 */
@Service
@Transactional(readOnly = true)
public class TradeService {

	/** Highest volume (quantity) first; tradeId keeps equal volumes in a stable order. */
	private static final Sort BY_VOLUME_DESC = Sort.by(Sort.Order.desc("quantity"), Sort.Order.asc("tradeId"));

	private final TradeRepository tradeRepository;

	public TradeService(TradeRepository tradeRepository) {
		this.tradeRepository = tradeRepository;
	}

	/** All trades, highest volume first. */
	public List<TradeResponse> getAllTradesByVolume() {
		return tradeRepository.findAllBy(BY_VOLUME_DESC).stream()
				.map(TradeResponse::from)
				.toList();
	}
}
