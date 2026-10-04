package com.equity.reports.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equity.reports.entity.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

	/** All trades with their customer fetched in the same query (avoids one query per trade). */
	@EntityGraph(attributePaths = "customer")
	List<Trade> findAllBy(Sort sort);
}
