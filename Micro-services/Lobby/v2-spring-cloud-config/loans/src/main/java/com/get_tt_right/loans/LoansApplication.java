package com.get_tt_right.loans;

import com.get_tt_right.loans.dto.LoansContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
/*@ComponentScans({ @ComponentScan("com.eazybytes.loans.controller") })
@EnableJpaRepositories("com.eazybytes.loans.repository")
@EntityScan("com.eazybytes.loans.model")*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")

// LoansContactInfoDto.class is the class name of the record class where we are trying to map all the properties from application.yml to the Pojo class. You can check more information about this class in the LoansContactInfoDto.java file.
@EnableConfigurationProperties(value = {LoansContactInfoDto.class}) // This annotation is used to tell the spring boot framework that please activate the configuration properties feature and please read the configuration properties from the class LoansContactInfoDto.
@OpenAPIDefinition(
		info = @Info(
				title = "Loans microservice REST API Documentation",
				description = "EazyBank Loans microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Colince Temoi",
						email = "colincetemoi190@gmail.com",
						url = "https://github.com/Colince-Temoi"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://github.com/Colince-Temoi"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "EazyBank Loans microservice REST API Documentation",
				url = "http://localhost:8090/swagger-ui/index.html"
		)
)
public class LoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoansApplication.class, args);
	}
}
