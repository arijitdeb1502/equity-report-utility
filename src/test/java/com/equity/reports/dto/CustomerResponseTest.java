package com.equity.reports.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CustomerResponseTest {

	@Test
	void maskKeepsLastFourCharacters() {
		assertThat(CustomerResponse.mask("ATWPM9089H")).isEqualTo("XXXXXX089H");
		assertThat(CustomerResponse.mask("IN30341700826232")).isEqualTo("XXXXXXXXXXXX6232");
	}

	@Test
	void maskLeavesNullAndShortValuesUnchanged() {
		assertThat(CustomerResponse.mask(null)).isNull();
		assertThat(CustomerResponse.mask("1234")).isEqualTo("1234");
	}
}
