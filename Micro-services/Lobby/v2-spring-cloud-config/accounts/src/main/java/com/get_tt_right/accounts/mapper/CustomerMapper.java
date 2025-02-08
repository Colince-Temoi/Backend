package com.get_tt_right.accounts.mapper;


import com.get_tt_right.accounts.dto.CustomerDto;
import com.get_tt_right.accounts.entity.Customer;
/**
 * A class containing methods to map Customer objects to CustomerDto objects and vice versa.
 * Are there any libraries that can automate this? Yes, there are libraries like MapStruct and ModelMapper that can automate this process.
 * We will not use them in this project because they are not officially recommended by Spring. As an example, Lombok is officially recommended by Spring and that's why when creating a project from spring.io if you search for lombok on the dependency management page, you will find it.
 * Your leads may not agree to bring such libraries into the project because they are not officially recommended by Spring as they are open source libraries and we never know if there are any security vulnerabilities in them. Be cautious while using such libraries.
 * For this project we will use this old style where we have complete control over the code. Think of a scenario where for example I want to send the mobile number to one of the clients and masking it by displaying only the last 4 digits.
 * Such kind of serialization logic we can write inside this mapper class. This kind of flexibility we may not get with the libraries like MapStruct or ModelMapper.
 * */
public class CustomerMapper {

    /**
     * Maps the given Customer object to a CustomerDto object.
     *
     * @param customer      - The Customer object to be mapped.
     * @param customerDto   - The CustomerDto object to be populated with values from the given Customer object.
     *
     * @return The populated CustomerDto object.
     */
    public static CustomerDto mapToCustomerDto(Customer customer, CustomerDto customerDto) {
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        return customerDto;
    }

    /**
     * Maps the given CustomerDto object to a Customer object.
     *
     * @param customerDto - The CustomerDto object containing data to be mapped.
     * @param customer    - The Customer object to be populated with values from the given CustomerDto object.
     *
     * @return The populated Customer object.
     */
    public static Customer mapToCustomer(CustomerDto customerDto, Customer customer) {
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        return customer;
    }

}