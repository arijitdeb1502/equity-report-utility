package com.equity.reports.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.equity.reports.dto.TopTradersReport;
import com.equity.reports.service.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports", description = "Customer and trading reports")
public class ReportController {

	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	@GetMapping("/top-traders")
	@Operation(summary = "Customers with the most trades in a date range",
			description = "Ranks customers by number of executed/settled trades with a trade date between "
					+ "'from' and 'to' (both inclusive). Cancelled and failed trades are not counted. "
					+ "Customers with the same trade count share a rank, and customers tied at the cutoff "
					+ "are all included, so the list can be longer than 'limit'.")
	@ApiResponse(responseCode = "200", description = "Ranked customers (empty list if nobody traded in the range)")
	@ApiResponse(responseCode = "400", description = "Missing or invalid dates, 'from' after 'to', or 'limit' outside 1-100")
	public TopTradersReport getTopTraders(
			@Parameter(description = "Start date (inclusive), yyyy-MM-dd", example = "2026-07-01", required = true)
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@Parameter(description = "End date (inclusive), yyyy-MM-dd", example = "2026-09-30", required = true)
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
			@Parameter(description = "Number of customers to return (1-100)", example = "10")
			@RequestParam(defaultValue = "10") @Min(1) @Max(100) int limit) {
		return reportService.getTopTraders(from, to, limit);
	}
}
