package com.get_tt_right.accounts.service.client;

import com.get_tt_right.accounts.dto.LoansDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**Update as of 11/04/2025
 *As of now, my fallback business logic is to return null when the circuit is open or there is an error in the upstream server
 * Instead of throwing a RTE to the downstream servers like Accounts ms to Gateway server and finally to the client, I'm going to send the null information related to Loans and with this at least my client application will receive Accounts and Cards related information.
 *  In the scenario you din't have this fallback mechanism, if 2 ms's are working properly i.e., Accounts and Cards but Loans ms is not working due to some transient issues, then the overall response is going to be a RTE. We are trying to avoid that with the help of this fallback mechanism and circuit breaker pattern.
 * We have created a similar fallback implementation for CardsFeignClient interface as well.
 * In your real projects, you can write your own fallback logic as per your needs. I.e., You could have a fallback logic to return some values from the cache or maybe you want to read the details from a different DB. It is up to you as you have the complete power.
 * */
@Component
public class LoansFallback implements LoansFeignClient{
    @Override
    public ResponseEntity<LoansDto> fetchLoanDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
