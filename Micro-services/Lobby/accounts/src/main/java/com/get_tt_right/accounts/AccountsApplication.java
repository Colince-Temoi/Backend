package com.get_tt_right.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl") // In this way we are telling the Spring Data JPA framework that please activate the Spring Data Jpa auditing feature and leverage the bean with the name  auditAwareImpl to understand the auditor.
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
