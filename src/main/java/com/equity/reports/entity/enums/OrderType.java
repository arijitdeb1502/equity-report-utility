package com.equity.reports.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Order type of a trade (trade.order_type).
 * The database value "SL-M" is not a valid Java identifier, so each constant
 * carries its database value and is mapped by {@link OrderTypeConverter}.
 * The API also uses the database value (e.g. "SL-M"), via {@link JsonValue}.
 */
public enum OrderType {
	MARKET("MARKET"),
	LIMIT("LIMIT"),
	SL("SL"),
	SL_M("SL-M");

	private final String dbValue;

	OrderType(String dbValue) {
		this.dbValue = dbValue;
	}

	@JsonValue
	public String getDbValue() {
		return dbValue;
	}

	public static OrderType fromDbValue(String dbValue) {
		for (OrderType type : values()) {
			if (type.dbValue.equals(dbValue)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown order type: " + dbValue);
	}
}
