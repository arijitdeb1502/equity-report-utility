package com.equity.reports.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.equity.reports.entity.Customer;
import com.equity.reports.entity.Trade;
import com.equity.reports.entity.enums.Exchange;
import com.equity.reports.entity.enums.OrderType;
import com.equity.reports.entity.enums.ProductType;
import com.equity.reports.entity.enums.TradeSide;
import com.equity.reports.entity.enums.TradeStatus;

import io.swagger.v3.oas.annotations.media.Schema;

/** Trade as returned by the API, with the customer's code and name. */
@Schema(name = "Trade", description = "An equity trade of a customer")
public record TradeResponse(
		@Schema(example = "1") Long tradeId,
		@Schema(example = "NSE20260701000001") String tradeRefNo,
		@Schema(example = "ORD20260701000001") String orderId,
		@Schema(example = "63") Long customerId,
		@Schema(example = "C00063") String customerCode,
		@Schema(example = "Arijit Mehta") String customerName,
		@Schema(example = "INFY") String symbol,
		@Schema(example = "INE009A01021") String isin,
		Exchange exchange,
		TradeSide side,
		OrderType orderType,
		ProductType productType,
		@Schema(description = "Volume: number of shares traded", example = "57") Integer quantity,
		@Schema(example = "1152.5000") BigDecimal price,
		@Schema(description = "quantity x price", example = "65692.5000") BigDecimal tradeValue,
		@Schema(example = "19.71") BigDecimal brokerage,
		@Schema(example = "81.47") BigDecimal taxes,
		@Schema(example = "INR") String currency,
		TradeStatus tradeStatus,
		LocalDateTime tradeTime,
		LocalDate tradeDate,
		LocalDate settlementDate) {

	public static TradeResponse from(Trade trade) {
		Customer customer = trade.getCustomer();
		return new TradeResponse(
				trade.getTradeId(),
				trade.getTradeRefNo(),
				trade.getOrderId(),
				customer.getCustomerId(),
				customer.getCustomerCode(),
				customer.getFullName(),
				trade.getSymbol(),
				trade.getIsin(),
				trade.getExchange(),
				trade.getSide(),
				trade.getOrderType(),
				trade.getProductType(),
				trade.getQuantity(),
				trade.getPrice(),
				trade.getTradeValue(),
				trade.getBrokerage(),
				trade.getTaxes(),
				trade.getCurrency(),
				trade.getTradeStatus(),
				trade.getTradeTime(),
				trade.getTradeDate(),
				trade.getSettlementDate());
	}
}
