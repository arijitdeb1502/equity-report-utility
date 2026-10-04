package com.equity.reports.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equity.reports.dto.TradeResponse;
import com.equity.reports.service.TradeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/trades")
@Tag(name = "Trades", description = "Trade lookups")
public class TradeController {

	private final TradeService tradeService;

	public TradeController(TradeService tradeService) {
		this.tradeService = tradeService;
	}

	@GetMapping("/by-volume")
	@Operation(summary = "Get all trades sorted by volume",
			description = "Returns every trade (all statuses), highest volume (quantity of shares) first. "
					+ "Trades with the same volume are ordered by trade id.")
	@ApiResponse(responseCode = "200", description = "List of trades (empty if there are none)")
	public List<TradeResponse> getAllTradesByVolume() {
		return tradeService.getAllTradesByVolume();
	}
}
