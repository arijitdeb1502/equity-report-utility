package com.equity.reports.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

/** One customer in the top-traders report. */
@Schema(name = "TopTrader", description = "A customer ranked by number of trades in the date range")
public record TopTraderResponse(
		@Schema(description = "1 = most trades; customers with the same trade count share a rank", example = "1") int rank,
		@Schema(example = "54") Long customerId,
		@Schema(example = "C00054") String customerCode,
		@Schema(example = "Aishwarya Malhotra") String customerName,
		@Schema(description = "Number of executed/settled trades (fills)", example = "31") long tradeCount,
		@Schema(description = "Number of distinct orders (an order can be filled in several trades)", example = "28") long orderCount,
		@Schema(description = "Total shares bought and sold", example = "15420") long totalVolume,
		@Schema(description = "Total value bought and sold (quantity x price)", example = "4523110.2500") BigDecimal totalTradeValue) {
}
