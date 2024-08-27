package com.eazybytes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    /* For now we are just returning a string, but in real time we will be fetching the balance details from the DB
     * and returning the same to the user.
    * This will include the transaction details as well.
    *  */
    @GetMapping("/myBalance")
    public  String getBalanceDetails () {
        return "Here are the balance details from the DB";
    }

}
