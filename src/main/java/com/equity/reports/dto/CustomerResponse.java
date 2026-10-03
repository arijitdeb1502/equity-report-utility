package com.equity.reports.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.equity.reports.entity.Customer;
import com.equity.reports.entity.enums.AccountStatus;
import com.equity.reports.entity.enums.KycStatus;
import com.equity.reports.entity.enums.RiskProfile;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Customer as returned by the API. PAN and demat numbers are masked
 * so that list responses do not expose full identity/account numbers.
 */
@Schema(name = "Customer", description = "A customer of the equity trading app")
public record CustomerResponse(
		@Schema(example = "1") Long customerId,
		@Schema(example = "C00001") String customerCode,
		@Schema(example = "Arijit") String firstName,
		@Schema(example = "Mehta") String lastName,
		@Schema(example = "arijit.mehta@example.com") String email,
		@Schema(example = "+91 9362362472") String phone,
		LocalDate dateOfBirth,
		@Schema(description = "Masked PAN", example = "XXXXXX089H") String panNumber,
		@Schema(description = "Masked demat account number", example = "XXXXXXXXXXXX6232") String dematAccountNo,
		String addressLine1,
		String addressLine2,
		@Schema(example = "Pune") String city,
		@Schema(example = "Maharashtra") String state,
		@Schema(example = "India") String country,
		@Schema(example = "411001") String postalCode,
		KycStatus kycStatus,
		AccountStatus accountStatus,
		RiskProfile riskProfile,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {

	public static CustomerResponse from(Customer customer) {
		return new CustomerResponse(
				customer.getCustomerId(),
				customer.getCustomerCode(),
				customer.getFirstName(),
				customer.getLastName(),
				customer.getEmail(),
				customer.getPhone(),
				customer.getDateOfBirth(),
				mask(customer.getPanNumber()),
				mask(customer.getDematAccountNo()),
				customer.getAddressLine1(),
				customer.getAddressLine2(),
				customer.getCity(),
				customer.getState(),
				customer.getCountry(),
				customer.getPostalCode(),
				customer.getKycStatus(),
				customer.getAccountStatus(),
				customer.getRiskProfile(),
				customer.getCreatedAt(),
				customer.getUpdatedAt());
	}

	/** Keeps the last 4 characters, replaces the rest with X. */
	static String mask(String value) {
		if (value == null || value.length() <= 4) {
			return value;
		}
		return "X".repeat(value.length() - 4) + value.substring(value.length() - 4);
	}
}
