package com.get_tt_right.accounts.service.client;

import com.get_tt_right.accounts.dto.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/** Update as of 27/3/2025
 * Here, we have modified the fetchLoanDetails signature by introducing the correlationId parameter.
 * Here, just like we have @RequestParam String mobileNumber, as copied from the CustomerController fetchAccountDetails method, we have introduced @RequestHeader("eazybank-correlation-id") String correlationId. Copy this as it is exactly in the CustomerController fetchAccountDetails method.
 * */
@FeignClient(name = "loans")
public interface LoansFeignClient {
    @GetMapping(value = "/api/fetch", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestHeader("eazybank-correlation-id") String correlationId, @RequestParam String mobileNumber);
}
