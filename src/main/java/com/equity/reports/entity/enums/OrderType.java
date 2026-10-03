package com.equity.reports.entity.enums;

/**
 * Order type of a trade (trade.order_type).
 * The database value "SL-M" is not a valid Java identifier, so each constant
 * carries its database value and is mapped by {@link OrderTypeConverter}.
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
