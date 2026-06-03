package com.get_tt_right.customer.query;

import lombok.Value;

/** Here also, for the Class name, we need to follow the signature/standard - VERB+NOUN+Query. The VERB can be Find, Read, Get etc. followed by a NOUN and at last the suffix - Query
 * Inside this class, we are going to create a single String field which is mobileNumber because anyone who wants to know the customer details, they are going to send the mobile number as a request param and based upon the same we are going to fetch the data from the read DB. So, to support this requirement/functionality we have declared only a single field inside this class.
 * On top of this class I have mentioned an annotation which is @Value. This annotation is going to generate getter methods but not the setter methods - you can either check its documentation by Ctl + clicking on it or you can otherwise press Ctrl + F12 to see the members of this class, and you will be able to verify what we are talking about. Since we do not have any scenario to set a new value once this object is created, we are trying to avoid the @Data annotation, and instead we are going to use @Value annotation.
 *
 * */
@Value
public class FindCustomerQuery {
    private final String mobileNumber;
}
