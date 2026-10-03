package com.equity.reports.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import com.equity.reports.entity.enums.AccountStatus;
import com.equity.reports.entity.enums.KycStatus;
import com.equity.reports.entity.enums.RiskProfile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Read-only mapping of the trading app's {@code customer} table.
 * The reporting utility never writes, hence {@link Immutable} and no setters.
 */
@Entity
@Immutable
@Table(name = "customer")
public class Customer {

	@Id
	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "customer_code", nullable = false, length = 20)
	private String customerCode;

	@Column(name = "first_name", nullable = false, length = 100)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 100)
	private String lastName;

	@Column(name = "email", nullable = false, length = 150)
	private String email;

	@Column(name = "phone", length = 20)
	private String phone;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "pan_number", length = 10)
	private String panNumber;

	@Column(name = "demat_account_no", length = 20)
	private String dematAccountNo;

	@Column(name = "address_line1", length = 200)
	private String addressLine1;

	@Column(name = "address_line2", length = 200)
	private String addressLine2;

	@Column(name = "city", length = 100)
	private String city;

	@Column(name = "state", length = 100)
	private String state;

	@Column(name = "country", length = 60)
	private String country;

	@Column(name = "postal_code", length = 12)
	private String postalCode;

	@Enumerated(EnumType.STRING)
	@Column(name = "kyc_status", nullable = false, length = 20)
	private KycStatus kycStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "account_status", nullable = false, length = 20)
	private AccountStatus accountStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "risk_profile", length = 20)
	private RiskProfile riskProfile;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	protected Customer() {
		// for JPA
	}

	public Long getCustomerId() {
		return customerId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public String getDematAccountNo() {
		return dematAccountNo;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public KycStatus getKycStatus() {
		return kycStatus;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public RiskProfile getRiskProfile() {
		return riskProfile;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
