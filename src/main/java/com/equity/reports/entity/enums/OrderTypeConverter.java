package com.equity.reports.entity.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderTypeConverter implements AttributeConverter<OrderType, String> {

	@Override
	public String convertToDatabaseColumn(OrderType orderType) {
		return orderType == null ? null : orderType.getDbValue();
	}

	@Override
	public OrderType convertToEntityAttribute(String dbValue) {
		return dbValue == null ? null : OrderType.fromDbValue(dbValue);
	}
}
