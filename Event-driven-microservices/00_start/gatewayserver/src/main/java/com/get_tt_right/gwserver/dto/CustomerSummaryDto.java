package com.get_tt_right.gwserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** As you can see this class has some Secondary type fields defined i.e., customer of type CustomerDto, account of type AccountsDto, loan of type LoansDto and card of type CardsDto.
 * The object of this class is what we are going to return to the client applications.
 * */
@Data
@AllArgsConstructor
public class CustomerSummaryDto {

    private CustomerDto customer;
    private AccountsDto account;
    private LoansDto loan;
    private CardsDto card;

}