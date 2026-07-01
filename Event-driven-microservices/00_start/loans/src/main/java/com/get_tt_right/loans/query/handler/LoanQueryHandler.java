package com.get_tt_right.loans.query.handler;

import com.get_tt_right.loans.dto.LoansDto;
import com.get_tt_right.loans.query.FindLoanQuery;
import com.get_tt_right.loans.service.ILoansService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanQueryHandler {
    private final ILoansService iLoansService;

    @QueryHandler
    public LoansDto findLoan(FindLoanQuery query) {
        LoansDto loan = iLoansService.fetchLoan(query.getMobileNumber());
        return loan;
    }
}
