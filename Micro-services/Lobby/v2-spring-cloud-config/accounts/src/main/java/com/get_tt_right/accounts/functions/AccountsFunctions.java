package com.get_tt_right.accounts.functions;

import com.get_tt_right.accounts.service.IAccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * We have 3 types of functional interface i.e., Consumer, Supplier, Function. What Functional interface we are going to use here? Think about it. From our business scenario, the function that we are going to create is responsible to take the message from the message ms which is of type Long and as soon as it receives this, it has to update the status value inside the DB. Apart from that, it is not going to respond anything to my message ms. This means that it is always going to accept an output but not going to send any output.
 * In such scenarios, we need to leverage the Consumer Functional interface. So the function that we are going to implement has the signature + updateCommunication() : Consumer<Long>. Here the generic type of Consumer is Long which means that the input parameter that I am going to accept is Long and the output parameter that I am going to return is nothing. Inside this function, we need to write a Lambda implementation which is going to act as an implementation logic for this Consumer functional interface. So, I am writing a return statement since we need to return the lambda expression  then followed by the lambda implementation.
 * In order to update this communication status inside the DB, 1st we need to create a column inside the accounts table. For the same, go to the schema.sql file available inside the resources folder of accounts ms. Inside this file, you can see we are trying to create tables like Customer and Accounts whenever we are trying to start our ms. Since we are using h2 db, the table is going to be recreated everytime we start or restart our application. But for me I am using a real DB container. So, you can access the DB container from the client of your choice and add this new column i.e., communication_sw. Here 'sw' stands for 'switch'. It is going to be a boolean value. Also make sure to update the schema.sql file. with this column just in case you are running your application on a new clean DB container. Define this column just after the '`branch_address`' column.
 * As a next step, go to the Accounts entity class and add details related to the new field that I have created. There I have created a new field with the name 'communicationSw' and using @Column annotation, I have mapped it to the column with the name 'communication_sw' inside the DB for the table named 'accounts'. As a next step, we need to write some logic inside our accounts ms to update this column for a given account number. For the same, inside 'IAccountsService' I am going to create a new specification/rule/abstract method  with the signature boolean updateCommunicationStatus(Long accountNumber); For this abstract method, we need to write the implementation logic inside the AccountsServiceImpl class. So, since we need this implementation logic to the end of this class, put a cursor there and then go to the top of the class and on hovering on the class name, select the implement methods option and easily select rule you want to implement. This should create you an empty implementation that will give you the foundation to start writing your own logic. Check that methods docstring for more details.
 * To this class, inject the IAccountsService in the constructor and then use it to call the updateCommunicationStatus method. How will we do this?
 *  1. Mention the IAccountsService iAccountsService parameter in the updateCommunication function.(*Remember this function is like a class. Hope you can connect this from the knowledge you learnt while learning functional programming). Since we are going to mention @Bean on top of this function, whatever input parameter that we define to this method is going to be automatically injected/Autowired by the Spring framework at runtime. We don't need to mention any @Autowired annotation.
 *  2. The next step is super simple, after the log statement we need to call/invoke the new method that we have created which is updateCommunicationStatus method.
 * Hurreey! This should now be crisp clear to you.
 * */
@Configuration
public class AccountsFunctions {
    private static final Logger log = LoggerFactory.getLogger(AccountsFunctions.class);

    @Bean
    public Consumer<Long> updateCommunication(IAccountsService iAccountsService) {
        return (accountNumber) -> {
            log.info("Updating communication status for the account number {}", accountNumber);
            iAccountsService.updateCommunicationStatus(accountNumber);
        };
    }
}
