package com.get_tt_right.accounts;

import com.get_tt_right.accounts.command.interceptor.AccountsCommandInterceptor;
import com.get_tt_right.common.config.AxonConfig;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
/** This tels you that this is a Spring Boot Application as can be seen from the annotation @SpringBootApplication
 * Next I have annotated this class with @EnableJpaAuditing and inside this annotation I have passed the auditAwareImpl class as the auditorAwareRef in order to enable the auditing process.
 * */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@Import(AxonConfig.class)
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

    /**With the help of this method we are trying to configure the AccountsCommandInterceptor
     * */
    @Autowired
    public void registerCustomerCommandInterceptor(ApplicationContext context, CommandGateway commandGateway) {
        commandGateway.registerDispatchInterceptor(context.getBean(AccountsCommandInterceptor.class));
    }

    /**With the help of this method we are trying to configure how the errors need to be handled for the "account-group"
     * Here also just like we did in customer ms we are trying to use PropagatingErrorHandler
     * */
    @Autowired
    public void configure(EventProcessingConfigurer config) {
        config.registerListenerInvocationErrorHandler("account-group",
                conf -> PropagatingErrorHandler.instance());
    }

}
