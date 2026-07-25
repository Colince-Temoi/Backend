package com.get_tt_right.loans.service;

import com.get_tt_right.loans.command.event.LoanUpdatedEvent;
import com.get_tt_right.loans.dto.LoansDto;
import com.get_tt_right.loans.entity.Loans;

public interface ILoansService {

    /** Old Spec
     *
     * @param mobileNumber - Mobile Number of the Customer

    void createLoan(String mobileNumber);
     */
    /** New Spec
     * @param loan - Loans object
     */
    void createLoan(Loans loan);

    /**
     *
     * @param mobileNumber - Input mobile Number
     *  @return Loan Details based on a given mobileNumber
     */
    LoansDto fetchLoan(String mobileNumber);

    /** Old Spec
     *
     * @param loansDto - LoansDto Object
     * @return boolean indicating if the update of card details is successful or not

    boolean updateLoan(LoansDto loansDto);
     */
    /** New Spec
     * @param event - LoanUpdatedEvent Object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean updateLoan(LoanUpdatedEvent event);

    /**
     *
     * @param loanNumber - Input Loan Number
     * @return boolean indicating if the delete of loan details is successful or not
     */
    boolean deleteLoan(Long loanNumber);

}
