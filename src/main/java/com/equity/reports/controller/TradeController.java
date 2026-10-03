package com.equity.reports.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.equity.reports.service.TradeService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/trades")
@Tag(name = "Trades", description = "Trade lookups")
public class TradeController {

	private final TradeService tradeService;

	public TradeController(TradeService tradeService) {
		this.tradeService = tradeService;
	}
}
