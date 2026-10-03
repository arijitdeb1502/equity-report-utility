package com.equity.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equity.reports.entity.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
}
