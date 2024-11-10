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

    /** With the help of the custom finder method we are trying to change data inside the database.
     * Whenever you are trying to do modification to the database, with the custom methods that you have written, we need to make sure
     * we are mentioning 2 annotations - @Transactional and @Modifying.
     * @Modifying - Will tell to the Spring Data Jpa framework that this method will modify the database and that's why we are like;
     * please execute the query of this method in a transaction. So that if something goes wrong, we can rollback the transaction.That's why we are mentioning this @Transactional.
     * When Spring Data Jpa runs my query inside a transaction, and some error/issue happens at runtime. I mean any partial change of data that happens due to the queries that are executed inside the transaction, will be rolled back by the Spring Data Jpa framework.
     * Like this we will be in safe hands and won't find ourselves in a situation where the data is partially updated. I.e., we deleted an account but not the customer.
     * Those kind of situations we need to make sure we are handling with the help of the @Transactional and @Modifying annotations.
     *
     * Deletes an account by its customer ID.
     *
     * @param customerId The ID of the customer whose account to delete
     */
    @Transactional
    @Modifying
    void deleteByCustomerId(Long customerId);

}