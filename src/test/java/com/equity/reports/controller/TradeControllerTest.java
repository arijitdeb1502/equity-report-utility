package com.equity.reports.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.equity.reports.dto.TradeResponse;
import com.equity.reports.entity.enums.Exchange;
import com.equity.reports.entity.enums.OrderType;
import com.equity.reports.entity.enums.ProductType;
import com.equity.reports.entity.enums.TradeSide;
import com.equity.reports.entity.enums.TradeStatus;
import com.equity.reports.service.TradeService;

@WebMvcTest(TradeController.class)
class TradeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TradeService tradeService;

	@Test
	void getAllTradesByVolumeReturnsServiceOrder() throws Exception {
		when(tradeService.getAllTradesByVolume()).thenReturn(List.of(
				trade(2L, "INFY", 500, OrderType.SL_M),
				trade(1L, "TCS", 20, OrderType.MARKET)));

		mockMvc.perform(get("/api/v1/trades/by-volume"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].symbol").value("INFY"))
				.andExpect(jsonPath("$[0].quantity").value(500))
				.andExpect(jsonPath("$[0].orderType").value("SL-M"))
				.andExpect(jsonPath("$[0].customerCode").value("C00001"))
				.andExpect(jsonPath("$[1].quantity").value(20));
	}

	@Test
	void getAllTradesByVolumeReturnsEmptyList() throws Exception {
		when(tradeService.getAllTradesByVolume()).thenReturn(List.of());

		mockMvc.perform(get("/api/v1/trades/by-volume"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}

	private static TradeResponse trade(Long id, String symbol, int quantity, OrderType orderType) {
		BigDecimal price = new BigDecimal("1500.0000");
		return new TradeResponse(id, "NSE2026070100000" + id, "ORD2026070100000" + id, 1L, "C00001", "Arijit Mehta",
				symbol, null, Exchange.NSE, TradeSide.BUY, orderType, ProductType.CNC, quantity, price,
				price.multiply(BigDecimal.valueOf(quantity)), new BigDecimal("20.00"), new BigDecimal("50.00"), "INR",
				TradeStatus.SETTLED, LocalDateTime.of(2026, 7, 1, 10, 0), LocalDate.of(2026, 7, 1),
				LocalDate.of(2026, 7, 2));
	}
}
