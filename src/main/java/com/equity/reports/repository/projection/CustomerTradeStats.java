package com.equity.reports.repository.projection;

import java.math.BigDecimal;

/** Per-customer trade totals, built directly by the JPQL query in TradeRepository. */
public record CustomerTradeStats(
		Long customerId,
		String customerCode,
		String firstName,
		String lastName,
		Long tradeCount,
		Long orderCount,
		Long totalVolume,
		BigDecimal totalTradeValue) {
}
