package com.get_tt_right.loans.repository;

import com.get_tt_right.loans.entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/** Here we have defined 2 custom methods findByMobileNumber and findByLoanNumber to fetch the loans based on the mobile number and loan number respectively.
 * Like you know the PK inside the loans table is Loan Id. So, whenever we have such a requirement to fetch the loans based on the mobile number or loan number (Non PK column), we need to make sure that we are using these custom finder methods.
 * */
@Repository
public interface LoansRepository extends JpaRepository<Loans, Long> {

    Optional<Loans> findByMobileNumber(String mobileNumber);

    Optional<Loans> findByLoanNumber(String loanNumber);

}
