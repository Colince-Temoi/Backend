package com.get_tt_right.accounts.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/** This class has to implement the AuditorAware interface which is a generic interface, and we need to specify the type of the auditor.
 * Since our createdBy and updatedBy fields are of type String, we need to specify the type of the auditor as String.
 * We need to implement the getCurrentAuditor() method. We need to write logic in this method to make sure we are returning who the current auditor is so that my spring data jpa framework can leverage it to populate the createdBy and updatedBy fields whenever required/needed.
 * Inside this method, the logic is, instead of returning an empty value or null, we are returning a hardcoded string ACCOUNTS_MS. Here MS means microservice.
 * So, this is the value I need be considered by the Spring Data Jpa Framework to populate the createdBy and updatedBy fields.
 * later on when we integrate our microservice with the Spring Security framework, I will be showing you how to populate the logged-in user/client application.
 * Also, you need to make sure you are mentioning the @Component annotation on top of this class and giving it a name, auditAwareImpl. This is the bean name that I want to consider in my application.
 * The name can be anything, but it should be meaningful. Here I'm just trying to match the name with the class name.
 * After this go to the BaseEntity class and mention the annotation @EntityListeners(AuditingEntityListener.class) on top of the class.
 * */
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        //return Optional.empty();
        return Optional.of("ACCOUNTS_MS");
    }
	
}