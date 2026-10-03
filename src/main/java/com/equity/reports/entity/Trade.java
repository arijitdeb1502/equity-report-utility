package com.equity.reports.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.equity.reports.entity.enums.Exchange;
import com.equity.reports.entity.enums.OrderType;
import com.equity.reports.entity.enums.ProductType;
import com.equity.reports.entity.enums.TradeSide;
import com.equity.reports.entity.enums.TradeStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Read-only mapping of the trading app's {@code trade} table.
 * The reporting utility never writes, hence {@link Immutable} and no setters.
 */
@Entity
@Immutable
@Table(name = "trade")
public class Trade {

	@Id
	@Column(name = "trade_id")
	private Long tradeId;

	@Column(name = "trade_ref_no", nullable = false, length = 30)
	private String tradeRefNo;

	@Column(name = "order_id", nullable = false, length = 30)
	private String orderId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@Column(name = "symbol", nullable = false, length = 20)
	private String symbol;

	@Column(name = "isin", length = 12)
	private String isin;

	@Enumerated(EnumType.STRING)
	@Column(name = "exchange", nullable = false, length = 10)
	private Exchange exchange;

	@Enumerated(EnumType.STRING)
	@Column(name = "side", nullable = false, length = 4)
	private TradeSide side;

	// mapped by OrderTypeConverter (autoApply)
	@Column(name = "order_type", nullable = false, length = 10)
	private OrderType orderType;

	@Enumerated(EnumType.STRING)
	@Column(name = "product_type", nullable = false, length = 10)
	private ProductType productType;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "price", nullable = false, precision = 15, scale = 4)
	private BigDecimal price;

	// generated column in the database: quantity * price
	@Column(name = "trade_value", precision = 20, scale = 4, insertable = false, updatable = false)
	private BigDecimal tradeValue;

	@Column(name = "brokerage", nullable = false, precision = 12, scale = 4)
	private BigDecimal brokerage;

	@Column(name = "taxes", nullable = false, precision = 12, scale = 4)
	private BigDecimal taxes;

	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(name = "currency", nullable = false, length = 3)
	private String currency;

	@Enumerated(EnumType.STRING)
	@Column(name = "trade_status", nullable = false, length = 15)
	private TradeStatus tradeStatus;

	@Column(name = "trade_time", nullable = false)
	private LocalDateTime tradeTime;

	@Column(name = "trade_date", nullable = false)
	private LocalDate tradeDate;

	@Column(name = "settlement_date")
	private LocalDate settlementDate;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	protected Trade() {
		// for JPA
	}

	public Long getTradeId() {
		return tradeId;
	}

	public String getTradeRefNo() {
		return tradeRefNo;
	}

	public String getOrderId() {
		return orderId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getIsin() {
		return isin;
	}

	public Exchange getExchange() {
		return exchange;
	}

	public TradeSide getSide() {
		return side;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public ProductType getProductType() {
		return productType;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getTradeValue() {
		return tradeValue;
	}

	public BigDecimal getBrokerage() {
		return brokerage;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public String getCurrency() {
		return currency;
	}

	public TradeStatus getTradeStatus() {
		return tradeStatus;
	}

	public LocalDateTime getTradeTime() {
		return tradeTime;
	}

	public LocalDate getTradeDate() {
		return tradeDate;
	}

	public LocalDate getSettlementDate() {
		return settlementDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
