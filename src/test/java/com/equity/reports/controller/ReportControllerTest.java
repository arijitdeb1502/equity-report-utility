package com.equity.reports.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.equity.reports.dto.TopTraderResponse;
import com.equity.reports.dto.TopTradersReport;
import com.equity.reports.service.ReportService;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

	private static final LocalDate FROM = LocalDate.of(2026, 7, 1);
	private static final LocalDate TO = LocalDate.of(2026, 9, 30);

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReportService reportService;

	@Test
	void returnsTopTraders() throws Exception {
		when(reportService.getTopTraders(FROM, TO, 5)).thenReturn(new TopTradersReport(FROM, TO, 5, List.of(
				new TopTraderResponse(1, 54L, "C00054", "Aishwarya Malhotra", 31, 28, 15420,
						new BigDecimal("4523110.25")))));

		mockMvc.perform(get("/api/v1/reports/top-traders")
				.param("from", "2026-07-01").param("to", "2026-09-30").param("limit", "5"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.fromDate").value("2026-07-01"))
				.andExpect(jsonPath("$.toDate").value("2026-09-30"))
				.andExpect(jsonPath("$.customers[0].rank").value(1))
				.andExpect(jsonPath("$.customers[0].customerCode").value("C00054"))
				.andExpect(jsonPath("$.customers[0].tradeCount").value(31));
	}

	@Test
	void limitDefaultsToTen() throws Exception {
		when(reportService.getTopTraders(FROM, TO, 10)).thenReturn(new TopTradersReport(FROM, TO, 10, List.of()));

		mockMvc.perform(get("/api/v1/reports/top-traders").param("from", "2026-07-01").param("to", "2026-09-30"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.limit").value(10))
				.andExpect(jsonPath("$.customers.length()").value(0));
	}

	@Test
	void missingDateIsBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/reports/top-traders").param("from", "2026-07-01"))
				.andExpect(status().isBadRequest());
		verifyNoInteractions(reportService);
	}

	@Test
	void invalidDateFormatIsBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/reports/top-traders").param("from", "01-07-2026").param("to", "2026-09-30"))
				.andExpect(status().isBadRequest());
		verifyNoInteractions(reportService);
	}

	@Test
	void limitOutOfRangeIsBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/reports/top-traders")
				.param("from", "2026-07-01").param("to", "2026-09-30").param("limit", "0"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("'limit' must be greater than or equal to 1"));
		mockMvc.perform(get("/api/v1/reports/top-traders")
				.param("from", "2026-07-01").param("to", "2026-09-30").param("limit", "101"))
				.andExpect(status().isBadRequest());
		verifyNoInteractions(reportService);
	}

	@Test
	void fromAfterToIsBadRequest() throws Exception {
		when(reportService.getTopTraders(any(), any(), anyInt()))
				.thenThrow(new IllegalArgumentException("'from' (2026-09-30) must not be after 'to' (2026-07-01)"));

		mockMvc.perform(get("/api/v1/reports/top-traders").param("from", "2026-09-30").param("to", "2026-07-01"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.detail").value("'from' (2026-09-30) must not be after 'to' (2026-07-01)"));
	}
}
