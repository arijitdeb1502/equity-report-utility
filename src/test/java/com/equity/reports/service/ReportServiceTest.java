package com.equity.reports.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.equity.reports.dto.TopTraderResponse;
import com.equity.reports.dto.TopTradersReport;
import com.equity.reports.entity.enums.TradeStatus;
import com.equity.reports.repository.CustomerRepository;
import com.equity.reports.repository.TradeRepository;
import com.equity.reports.repository.projection.CustomerTradeStats;

class ReportServiceTest {

	private static final LocalDate FROM = LocalDate.of(2026, 7, 1);
	private static final LocalDate TO = LocalDate.of(2026, 9, 30);

	private final TradeRepository tradeRepository = mock(TradeRepository.class);
	private final ReportService reportService = new ReportService(mock(CustomerRepository.class), tradeRepository);

	@Test
	void ranksByTradeCountAndSharesRankOnTies() {
		givenStats(stats("C1", 30), stats("C2", 25), stats("C3", 25), stats("C4", 20));

		TopTradersReport report = reportService.getTopTraders(FROM, TO, 10);

		assertThat(report.customers()).extracting(TopTraderResponse::customerCode)
				.containsExactly("C1", "C2", "C3", "C4");
		assertThat(report.customers()).extracting(TopTraderResponse::rank).containsExactly(1, 2, 2, 4);
		assertThat(report.fromDate()).isEqualTo(FROM);
		assertThat(report.toDate()).isEqualTo(TO);
		assertThat(report.customers().get(0).customerName()).isEqualTo("First C1 Last");
	}

	@Test
	void includesCustomersTiedAtTheLimit() {
		givenStats(stats("C1", 30), stats("C2", 25), stats("C3", 25), stats("C4", 20));

		TopTradersReport report = reportService.getTopTraders(FROM, TO, 2);

		assertThat(report.customers()).extracting(TopTraderResponse::customerCode).containsExactly("C1", "C2", "C3");
		assertThat(report.limit()).isEqualTo(2);
	}

	@Test
	void stopsAtLimitWhenNoTie() {
		givenStats(stats("C1", 30), stats("C2", 25), stats("C3", 20));

		assertThat(reportService.getTopTraders(FROM, TO, 2).customers())
				.extracting(TopTraderResponse::customerCode).containsExactly("C1", "C2");
	}

	@Test
	void returnsEmptyListWhenNobodyTraded() {
		givenStats();

		assertThat(reportService.getTopTraders(FROM, TO, 10).customers()).isEmpty();
	}

	@Test
	void countsOnlyExecutedAndSettledTrades() {
		givenStats();

		reportService.getTopTraders(FROM, TO, 10);

		verify(tradeRepository).findTradeStatsPerCustomer(FROM, TO, Set.of(TradeStatus.EXECUTED, TradeStatus.SETTLED));
	}

	@Test
	void rejectsFromAfterTo() {
		assertThatThrownBy(() -> reportService.getTopTraders(TO, FROM, 10))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("must not be after");
		verifyNoInteractions(tradeRepository);
	}

	private void givenStats(CustomerTradeStats... rows) {
		when(tradeRepository.findTradeStatsPerCustomer(eq(FROM), eq(TO), any())).thenReturn(List.of(rows));
	}

	private static CustomerTradeStats stats(String code, long tradeCount) {
		return new CustomerTradeStats((long) code.hashCode(), code, "First " + code, "Last", tradeCount, tradeCount,
				tradeCount * 100, BigDecimal.valueOf(tradeCount * 1000));
	}
}
