package com.get_tt_right.loans.function;

import com.get_tt_right.common.dto.MobileNumberUpdateDto;
import com.get_tt_right.loans.service.ILoansService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class LoanFunctions {

    @Bean
    public Consumer<MobileNumberUpdateDto> updateLoanMobileNumber(ILoansService iLoansService) {
        return (mobileNumberUpdateDto) -> {
            log.info("Received  updateLoanMobileNumber request  for the details: {}", mobileNumberUpdateDto);
            iLoansService.updateMobileNumber(mobileNumberUpdateDto);
        };
    }

}