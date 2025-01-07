package com.get_tt_right.loans.service.impl;

import com.get_tt_right.loans.constants.LoansConstants;
import com.get_tt_right.loans.dto.LoansDto;
import com.get_tt_right.loans.entity.Loans;
import com.get_tt_right.loans.exception.LoanAlreadyExistsException;
import com.get_tt_right.loans.exception.ResourceNotFoundException;
import com.get_tt_right.loans.mapper.LoansMapper;
import com.get_tt_right.loans.repository.LoansRepository;
import com.get_tt_right.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {
// Injecting the LoansRepository dependency using the parameterized constructor.
    private LoansRepository loansRepository;

    /** Logic :
     * 1. Check if the loan already exists for the given mobile number.
     * 2. If it does not exist, create a new loan and save it to the database.
     * 3. If it does exist, throw a business exception, LoanAlreadyExistsException.
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> optionalLoans= loansRepository.findByMobileNumber(mobileNumber);
        if(optionalLoans.isPresent()){
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber "+mobileNumber);
        }
        loansRepository.save(createNewLoan(mobileNumber));
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new loan details
     */
    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000); // Generating a random 12 digit loan number
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN); // Setting the loan type to HOME_LOAN by default
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0); // Setting the amount paid to 0 by default - initial amount paid.
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }

    /** Logic :
     * 1. Fetch the loan details based on the given mobile number.
     * 2. If the loan details are not found, throw a ResourceNotFoundException.
     * 3. If the loan details are found, map the entity to the DTO and return the DTO.
     * @param mobileNumber - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    /** Logic :
     * 1. Fetch the loan details based on the given loan number. Since this is unique, we can use this to update the loan details.
     * 2. If the loan details are not found, throw a ResourceNotFoundException.
     * 3. If the loan details are found, update the loan details and save it to the database. First map the DTO to the entity and then save it.
     * 4. Return true if the update is successful.
     *
     * @param loansDto - LoansDto Object
     * @return boolean indicating if the update of loan details is successful or not
     */
    @Override
    public boolean updateLoan(LoansDto loansDto) {
        Loans loans = loansRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "LoanNumber", loansDto.getLoanNumber()));
        LoansMapper.mapToLoans(loansDto, loans);
        loansRepository.save(loans);
        return  true;
    }

    /** Logic :
     * 1. Fetch the loan details based on the given mobile number.
     * 2. If the loan details are not found, throw a ResourceNotFoundException.
     * 3. If the loan details are found, delete the loan details from the database. To deleteById, we need the loanId (PK of the table).
     * @param mobileNumber - Input MobileNumber
     * @return boolean indicating if the delete of loan details is successful or not
     */
    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        loansRepository.deleteById(loans.getLoanId());
        return true;
    }


}
