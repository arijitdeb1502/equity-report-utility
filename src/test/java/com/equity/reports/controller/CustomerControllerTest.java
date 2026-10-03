package com.equity.reports.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.equity.reports.dto.CustomerResponse;
import com.equity.reports.entity.enums.AccountStatus;
import com.equity.reports.entity.enums.KycStatus;
import com.equity.reports.entity.enums.RiskProfile;
import com.equity.reports.service.CustomerService;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerService customerService;

	@Test
	void getAllCustomersReturnsList() throws Exception {
		CustomerResponse customer = new CustomerResponse(1L, "C00001", "Arijit", "Mehta", "arijit.mehta@example.com",
				"+91 9362362472", LocalDate.of(1990, 12, 17), "XXXXXX089H", "XXXXXXXXXXXX6232", "414, Brigade Road",
				null, "Pune", "Maharashtra", "India", "411001", KycStatus.VERIFIED, AccountStatus.ACTIVE,
				RiskProfile.HIGH, LocalDateTime.of(2025, 4, 21, 22, 21, 6), LocalDateTime.of(2025, 4, 21, 22, 21, 6));
		when(customerService.getAllCustomers()).thenReturn(List.of(customer));

		mockMvc.perform(get("/api/v1/customers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].customerCode").value("C00001"))
				.andExpect(jsonPath("$[0].panNumber").value("XXXXXX089H"))
				.andExpect(jsonPath("$[0].kycStatus").value("VERIFIED"))
				.andExpect(jsonPath("$[0].dateOfBirth").value("1990-12-17"));
	}

	@Test
	void getAllCustomersReturnsEmptyList() throws Exception {
		when(customerService.getAllCustomers()).thenReturn(List.of());

		mockMvc.perform(get("/api/v1/customers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(0));
	}
}
