package com.get_tt_right.loans.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;
/** Here, since inside the application.yaml wea re going to have a prefix as loans for loans microservice, we need to make sure we are using the same in this annotation.
 * Else, every other explanation inside the AccountsContactInfoDto.java file is the same as well here.
 * As in, it is a record class >> It is going to accept different fields by following the data types and the names of the fields as per the application.yml file.
 * To activate these configuration properties, we need to use the @EnableConfigurationProperties annotation in the main class of the microservice - LoansApplication. Check the LoansApplication.java file.
 * */
@ConfigurationProperties(prefix = "loans")
public record LoansContactInfoDto(String message, Map<String, String> contactDetails, List<String> onCallSupport) {
}