package com.get_tt_right.accounts.dto;

import lombok.Data;
@Data
public class CustomerDto {
    private String name;
    private String email;
    private String mobileNumber;
    private AccountsDto accountsDto;
}