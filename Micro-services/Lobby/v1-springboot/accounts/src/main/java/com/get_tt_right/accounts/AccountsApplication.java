package com.get_tt_right.accounts;

import com.get_tt_right.accounts.dto.AccountsContactInfoDto;
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
/*@ComponentScans({ @ComponentScan("com.eazybytes.accounts.controller") })  // Using this, we need to mention all the packages where our beans are present. Like for example we have beans under the controller package. Similarly, we have beans inside the service package ... etc.
                                                                            // So, all those packages we need to repeat with the help of this @ComponentScan annotation inside @ComponentScans annotation.
@EnableJpaRepositories("com.eazybytes.accounts.repository")  // To mention where all your repositories are like jpa repositories and Jpa entities.
@EntityScan("com.eazybytes.accounts.model")

// I have commented out the above 3 annotations because I don't need to mention them. Reason, I maintained my sub-packages inside the main class package.
*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl") // In this way we are telling the Spring Data JPA framework that please activate the Spring Data Jpa auditing feature and leverage the bean with the name  auditAwareImpl to understand the auditor.
// AccountsContactInfoDto.class is the class name of the record class where we are trying to map all the properties from application.yml to the Pojo class. You can check more information about this class in the AccountsContactInfoDto.java file.
@EnableConfigurationProperties(value = {AccountsContactInfoDto.class}) // This annotation is used to tell the spring boot framework that please activate the configuration properties feature and please read the configuration properties from the class AccountsContactInfoDto.
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts microservice REST API Documentation",
				description = "EazyBank Accounts microservice REST API Documentation",
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
				description =  "EazyBank Accounts microservice REST API Documentation",
				url = "http://localhost:8080/swagger-ui/index.html"
		)
)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
