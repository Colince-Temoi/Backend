package com.get_tt_right.accounts.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
/** Here, we have audit related logic which is going to help us to audit the created date, updated date, deleted date, created by, updated by and deleted by columns inside our DB tables.
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
        return Optional.of("ACCOUNT_MS");
    }
	
}
