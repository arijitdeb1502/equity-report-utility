package com.equity.reports.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equity.reports.dto.TopTraderResponse;
import com.equity.reports.dto.TopTradersReport;
import com.equity.reports.entity.enums.TradeStatus;
import com.equity.reports.repository.CustomerRepository;
import com.equity.reports.repository.TradeRepository;
import com.equity.reports.repository.projection.CustomerTradeStats;

/**
 * Builds reports that combine customer and trade data.
 */
@Service
@Transactional(readOnly = true)
public class ReportService {

	/** Trades that actually happened; CANCELLED and FAILED trades are not counted. */
	private static final Set<TradeStatus> COMPLETED_STATUSES = EnumSet.of(TradeStatus.EXECUTED, TradeStatus.SETTLED);

	private final CustomerRepository customerRepository;
	private final TradeRepository tradeRepository;

	public ReportService(CustomerRepository customerRepository, TradeRepository tradeRepository) {
		this.customerRepository = customerRepository;
		this.tradeRepository = tradeRepository;
	}

	/**
	 * Customers with the most completed trades between {@code from} and {@code to} (inclusive).
	 * Customers with equal trade counts share a rank (1, 2, 2, 4, ...). If customers tie at the
	 * {@code limit} cutoff they are all included, so the list can be longer than {@code limit}.
	 */
	public TopTradersReport getTopTraders(LocalDate from, LocalDate to, int limit) {
		if (from.isAfter(to)) {
			throw new IllegalArgumentException("'from' (" + from + ") must not be after 'to' (" + to + ")");
		}
		List<CustomerTradeStats> stats = tradeRepository.findTradeStatsPerCustomer(from, to, COMPLETED_STATUSES);

		List<TopTraderResponse> customers = new ArrayList<>();
		int rank = 0;
		long previousCount = -1;
		for (int i = 0; i < stats.size(); i++) {
			CustomerTradeStats row = stats.get(i);
			if (row.tradeCount() != previousCount) {
				if (i >= limit) {
					break; // past the limit and not tied with the last included customer
				}
				rank = i + 1;
				previousCount = row.tradeCount();
			}
			customers.add(new TopTraderResponse(rank, row.customerId(), row.customerCode(),
					row.firstName() + " " + row.lastName(), row.tradeCount(), row.orderCount(),
					row.totalVolume(), row.totalTradeValue()));
		}
		return new TopTradersReport(from, to, limit, customers);
	}
}
