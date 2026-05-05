package com.get_tt_right.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
/** This tels you that this is a Spring Boot Application as can be seen from the annotation @SpringBootApplication
 * Next I have annotated this class with @EnableJpaAuditing and inside this annotation I have passed the auditAwareImpl class as the auditorAwareRef in order to enable the auditing process.
 * */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

}
