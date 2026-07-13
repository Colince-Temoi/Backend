package com.get_tt_right.customer.service.impl;

import com.get_tt_right.common.dto.MobileNumberUpdateDto;
import com.get_tt_right.customer.constants.CustomerConstants;
import com.get_tt_right.customer.dto.CustomerDto;
import com.get_tt_right.customer.entity.Customer;
import com.get_tt_right.customer.exception.CustomerAlreadyExistsException;
import com.get_tt_right.customer.exception.ResourceNotFoundException;
import com.get_tt_right.customer.mapper.CustomerMapper;
import com.get_tt_right.customer.repository.CustomerRepository;
import com.get_tt_right.customer.service.ICustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final StreamBridge streamBridge;

    @Override
    public void createCustomer(CustomerDto customerDto) {
        customerDto.setActiveSw(CustomerConstants.ACTIVE_SW);
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumberAndActiveSw(
                customerDto.getMobileNumber(), true);
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + customerDto.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
    }

    @Override
    public CustomerDto fetchCustomer(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumberAndActiveSw(mobileNumber, true).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        return customerDto;
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        Customer customer = customerRepository.findByMobileNumberAndActiveSw(customerDto.getMobileNumber(), true)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", customerDto.getMobileNumber()));
        CustomerMapper.mapToCustomer(customerDto, customer);
        customerRepository.save(customer);
        return true;
    }

    @Override
    public boolean deleteCustomer(String customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "customerId", customerId)
        );
        customer.setActiveSw(CustomerConstants.IN_ACTIVE_SW);
        customerRepository.save(customer);
        return true;
    }

    /** Inside this method, what I can do is, I can try to load the customer details based upon the given mobile number. Such code we already have somewhere inside this class - Just copy and paste it as is to this method.
     * I am passing the current mobile number. If there is no customer for this current mobile number then I want to throw this ResourceNotFoundException. If valid customer is available with the given current mobile number, then immediately using the same Customer entity object I want to set the new mobile number and towards the end I can try to invoke the CustomerRepository#save method. At the end we can try to pass true as the boolean value.
     * Up to where we have reached in the logic impl - the new mobile number will only be updated in the customer ms DB. But we don't want to stop here itself. As a next step, we want this Customer ms to publish an event or to forward the request to the Account ms as well to update the mobile number in it's own DB. How to do that? It is very simple, with the help of Spring Cloud Functions and Spring Cloud Streaming we are going to implement Asynchronous communication or Event driven ms's. Here what I am going to do is, in CustomerServiceImpl - I am going to create a private method#updateAccountMobileNumber which is going to return void. Check its docstring for more details.
     *
     * Implementing Compensation Transactions
     * ----------------------------------------
     * Let's imagine, just after the save method, I got some RTE. To visualize the demo of RTE, what we are going to do is, we will try adding a line of code which is "throw new RuntimeException("Some error occurred while updating mobileNumber");".  And since there is a RTE, all the next lines of code will not be executed. So you can comment them out for now to resolve the CE's.
     * In this scenario, Spring data Jpa framework can roll back the txn that happened during the line of execution i.e., "customerRepository.save(customer)". But Spring Data Jpa won't do that automatically, to make this possible, we need to mention a method level annotation on this "updateMobileNumber" method i.e., @Transactional. When we mention this, what is going to happen is, the logic present inside this method#updateMobileNumber is going to be executed as a txn. If any RTE happens which results to a RTE or its child exception, then the txn will automatically be rolled back by the Spring Data Jpa framework.
     * Save these changes >> Do a build >> follow the steps documented under "Testing our services locally in ide - steps". Now, inside the postman, I will try to invoke the API which is "choreography-mobile-number". If you click on the send button, you should see we are getting some error i.e., "errorMessage": "Some error occurred while updating mobileNumber", which is exactly our expectation. But now, let's try to validate by invoking the fetchCustomerSummary request and see if the mobile number is updated in any of the ms's or the old mobile number still persists in all the ms's. So, you can start by passing the old mobile number and see - No changes have been persisted in regard to the new mobile number that we were trying to update. This clearly confirms that the new mobile number which might have been saved in the customer ms DB as a result of the line of code "customerRepository.save(customer)" is rolled back automatically by the customer ms.
     * Like this, with the changes that we have done so far, we are good with the scenario - if the exception happens inside the customer ms itself. Next, let's imagine the exception can happen inside the accounts ms. To visualize the logic, we need to go to the AccountsServiceImpl method#updateCardMobileNumber - check its docstring for more details.
     * Remember to comment the line of code which is "throw new RuntimeException("Some error occurred while updating mobileNumber");" inside the AccountsServiceImpl#updateCardMobileNumber method and uncomment the lines of code below it as we are done with Customer ms RTE scenario handling that could occur and how data consistency is going to be preserved.
     * */
    @Override
    @Transactional
    public boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        String currentMobileNum = mobileNumberUpdateDto.getCurrentMobileNumber();
        Customer customer = customerRepository.findByMobileNumberAndActiveSw(currentMobileNum,
                true).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", currentMobileNum)
        );
        customer.setMobileNumber(mobileNumberUpdateDto.getNewMobileNumber());
        customerRepository.save(customer);
//         throw new RuntimeException("Some error occurred while updating mobileNumber");
        updateAccountMobileNumber(mobileNumberUpdateDto);
        return true;
    }

    /**To this method we are going to pass MobileNumberUpdateDto object as an input. This method we are invoking it as soon as we are done saving the new mobile number inside the customer ms DB.
     * To publish an event to the queue, we don't have to worry much. We just have to inject a bean "StreamBridge" into this service impl class. Using this beans reference, I can forward the event object to one of the queues or exchange available inside the RabbitMQ.
     * So, using the StreamBridge#send method, I can forward the event to the queue. To this method, we need to mention what is the binding name along with what is the payload data that we want to send. The binding name that I want to mention here is "updateAccountMobileNumber-out-0". The same binding name I am going to configure inside the application.yml file as well. The payload data that I want to send is the MobileNumberUpdateDto object.
     * We can add some logs here to just trace through our entire flow and verify that it is working as expected/properly. What we will do is - we will 1st try to add an annotation of Lombok which is @Slf4j. This annotation will help us to add the log statements to our code as can be seen in the loggers below.
     * With the logic that we have written here, we are trying to send our data to the binding name "updateAccountMobileNumber-out-0" but this binding name details we are yet to configure that inside the application.yml file. Open application.yml file of customer ms and just after the rabbitmq related configurations, we are going to add one more property definition under the spring i.e., cloud.stream.bindings and under this we need to mention the same binding name that we have mentioned inside the StreamBridge#send method. i.e., updateAccountMobileNumber-out-0. So, whenever someone send a request to this binding name, I want the data to be forwarded to a specific destination and that's why under the binding name configuration we need to mention "destination" and to this property we are going to assign/mention the destination name as "update-account-mobile-number".
     * So with this what is going to happen? Inside the RabbitMQ, there will be a destination created with the name "update-account-mobile-number". To the same destination my request is going to be forwarded. Now, on the accounts ms side we need to make some configuration to listen to this destination and read the data from the same destination. Now, open the application.yml file of accounts ms and paste the entire spring.cloud.bindings.updateAccountMobileNumber-out-0.destination=update-account-mobile-number configuration that we have configured inside customer ms application.yml file. Now inside the configurations that we have pasted inside the accounts ms application.yml file, since accounts ms is going to accepts the request, we need to make some small change to the binding name from "updateAccountMobileNumber-out-0" to "updateAccountMobileNumber-in-0". This is the standard naming convention that you need to follow inside the Spring Cloud functions and Spring Cloud Stream.
     * Under that binding name we need to make sure we are mentioning the same destination name that we have mentioned inside the customer ms application.yml file. i.e., update-account-mobile-number. Once the destination is mentioned, inside the accounts ms application.yml file, we need to mention one more configuration here which is "group" - to this group, I am assigning a value by reading the property which is spring.application.name. which we have already configured here in the accounts ms application.yml file. i.e., accounts. The reason that we need to mention "group" property for the input binding but not for the output binding is - it is very simple, in real applications we will end up having multiple instances of ms's. Here my accounts ms is acting as a consumer - if there are 10 different instances of accounts ms running behind the scenes, I want all them to be registered with the same group name which is accounts itself. When they are registered with the same group name what is going to happen behind the scenes is, they are going to act as a consumer group. When a consumer group is formed, the Spring cloud stream will make sure that the request is only forwarded to only one of the instances of the consumer ms which is accounts. Whereas on the customer side we don't have to mention because right now customer ms is a producer and a producer should not have any group details because while they are producing a message, the request is already being processed by one of the ms instances. Once this producer publishes the message, it will reach to the RabbitMQ. Since there is no need of load balancing here on the producer side - we don't need to mention "group" property/details here. Whereas since on the consumer side load balancing is required and since we want to make sure only one of the consumer ms instance need to process the request, that's why we need to mention this "group" property.
     * With the configurations that we have just mentioned, what will happen is - the Spring cloud stream and rabbitmq, they are going to work together, and they will try to forward the request that will come to the destination "update-account-mobile-number"  to the binding "updateAccountMobileNumber-in-0".  But with that only, the story is not going to end! haha. Once the request is received by this binding, we need to give a clue to the Spring cloud stream to which Java method or to which REST API, it needs to forward the request. In other words, we need to convey to the Spring cloud Stream, where is the business logic to process the request that is received by the binding "updateAccountMobileNumber-in-0". So, from the destination, "update-account-mobile-number", the message will be read but the message need to be executed by some business logic. To convey the same what we are going to do is - we are going to make some function configurations, how? just below the cloud property, I am going to mention a property function.definition to this we need to mention a value which is "updateAccountMobileNumber". Please make sure the function definition is same as the prefix that we have used inside the input binding name. i.e., updateAccountMobileNumber-in-0. If you follow this naming convention, then automatically the Spring cloud stream will know to which function it has to forward the request that is going to be received by this input binding i.e., updateAccountMobileNumber-in-0. Now, I have created a Spring cloud function definition. As a next step, I need to create a function which is going to have all the business logic.
     * Now, inside the accounts ms, I am going to create a package with the name "function" and inside this, i am going to create a java class i.e., AccountFunctions. Inside this class we can create any number of functions. Check out this class for more details.
     * */
    private void updateAccountMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending updateAccountMobileNumber request for the details: {}", mobileNumberUpdateDto); // This entire object is going to be printed on the console along with the log statement.
        var result = streamBridge.send("updateAccountMobileNumber-out-0",mobileNumberUpdateDto);
        log.info("Is the updateAccountMobileNumber request successfully triggered ? : {}", result); // This logger is going to print Is the updateAccountMobileNumber request successfully triggered or not. To this logger we are printing a variable "result". The data inside this "result" variable we are catching from the StreamBridge#send method output. If you navigate into the method#send, you will be able to see that its return type is boolean. The same boolean, we are going to print onto the console to confirm whether the data is sent to the accounts ms or not with the help of the given binding name. i.e., "updateAccountMobileNumber-out-0".
    }

    /**If you clearly notice, the code we have written inside updateMobileNumber is very similar to what we are writing here.
     * The only trick that you need to be aware is that by the time this rollbackMobileNumber is being executed, the old mobile number is already replaced with the new mobile number as part of the updateMobileNumber method. So when you want to roll back/undo that, you need to load the existing data from the DB using the new mobile number haha!
     * Once the existing record is fetched, we need to set the old mobile number by using the getCurrentMobileNumber method. It like we are just reversing things - no rocket science.
     * At last, we need to invoke the save method to save the changes to the DB, and we don't need to invoke the private method#updateAccountMobileNumber like we did in the updateMobileNumber method. Simple!
     * So, this is going to complete the compensation txn on the customer ms.
     * */
    @Override
    public boolean rollbackMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        String newMobileNumber = mobileNumberUpdateDto.getNewMobileNumber();
        Customer customer = customerRepository.findByMobileNumberAndActiveSw(newMobileNumber,
                true).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", newMobileNumber)
        );
        customer.setMobileNumber(mobileNumberUpdateDto.getCurrentMobileNumber());
        customerRepository.save(customer);
        return true;
    }


}
