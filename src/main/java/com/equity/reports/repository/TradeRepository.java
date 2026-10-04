package com.equity.reports.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.equity.reports.entity.Trade;
import com.equity.reports.entity.enums.TradeStatus;
import com.equity.reports.repository.projection.CustomerTradeStats;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

	/** All trades with their customer fetched in the same query (avoids one query per trade). */
	@EntityGraph(attributePaths = "customer")
	List<Trade> findAllBy(Sort sort);

	/**
	 * Trade totals per customer for trades dated between {@code from} and {@code to} (inclusive)
	 * with one of the given statuses, most trades first. Aggregated in the database:
	 * returns one row per customer who traded in the range.
	 */
	@Query("""
			select new com.equity.reports.repository.projection.CustomerTradeStats(
			         c.customerId, c.customerCode, c.firstName, c.lastName,
			         count(t), count(distinct t.orderId), sum(t.quantity), sum(t.tradeValue))
			from Trade t join t.customer c
			where t.tradeDate between :from and :to
			  and t.tradeStatus in :statuses
			group by c.customerId, c.customerCode, c.firstName, c.lastName
			order by count(t) desc, c.customerCode asc
			""")
	List<CustomerTradeStats> findTradeStatsPerCustomer(@Param("from") LocalDate from, @Param("to") LocalDate to,
			@Param("statuses") Collection<TradeStatus> statuses);
}
