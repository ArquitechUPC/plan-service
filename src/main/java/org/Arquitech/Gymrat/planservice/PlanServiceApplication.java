package org.Arquitech.Gymrat.planservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@OpenAPIDefinition(
		info = @Info(
				title = "API Plan - GymRat",
				version = "1.0",
				description = "API to manage plans part of the application called GymRat"
		)
)
@EnableDiscoveryClient
@SpringBootApplication
public class PlanServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlanServiceApplication.class, args);
	}

}
