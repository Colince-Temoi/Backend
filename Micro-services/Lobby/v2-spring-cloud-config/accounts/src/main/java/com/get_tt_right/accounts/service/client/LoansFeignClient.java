package com.get_tt_right.accounts.service.client;

import com.get_tt_right.accounts.dto.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loans")
public interface LoansFeignClient {
    @GetMapping(value = "/api/fetch", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam String mobileNumber);
}
