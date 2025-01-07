package com.get_tt_right.accounts.repository;

import com.get_tt_right.accounts.entity.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    /**
     * Retrieves an account by its customer ID.
     *
     * @param customerId The ID of the customer whose account to retrieve
     * @return An Optional containing the account with the given customer ID, or an empty Optional if no such account exists
     */
    Optional<Accounts> findByCustomerId(Long customerId);

    /**
     * Deletes an account by its customer ID.
     *
     * @param customerId The ID of the customer whose account to delete
     */
    @Transactional
    @Modifying
    void deleteByCustomerId(Long customerId);

}