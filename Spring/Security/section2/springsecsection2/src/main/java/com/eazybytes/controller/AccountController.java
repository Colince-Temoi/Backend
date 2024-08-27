package com.eazybytes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    /* For now we are just returning a string, but in real time we will be fetching the account details from the DB
     * and returning the same to the user.
    * */
    @GetMapping("/myAccount")
    public  String getAccountDetails () {
        return "Here are the account details from the DB";
    }

}
