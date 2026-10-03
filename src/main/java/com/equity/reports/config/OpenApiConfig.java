package com.equity.reports.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI equityReportOpenApi() {
		return new OpenAPI().info(new Info()
				.title("Equity Report Utility API")
				.description("Reports generated from the Equity Trading App database (customers, trades)")
				.version("v1"));
	}
}
