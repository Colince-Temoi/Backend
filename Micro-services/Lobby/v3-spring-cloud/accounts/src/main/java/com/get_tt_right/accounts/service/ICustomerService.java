package com.get_tt_right.accounts.service;

import com.get_tt_right.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {
    /**
     * Fetches customer details based on a given mobile number
     *
     * @param mobileNumber the mobile number of the customer
     * @return the customer details
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber,String correlationId); // Signature/rule definition
}
