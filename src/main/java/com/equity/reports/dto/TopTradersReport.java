package com.equity.reports.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/** Customers with the most trades between two dates. */
@Schema(name = "TopTradersReport")
public record TopTradersReport(
		@Schema(example = "2026-07-01") LocalDate fromDate,
		@Schema(example = "2026-09-30") LocalDate toDate,
		@Schema(description = "Requested number of customers; more are returned if several tie at the cutoff", example = "10") int limit,
		List<TopTraderResponse> customers) {
}
