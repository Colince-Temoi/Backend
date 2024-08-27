package com.eazybytes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {

    /* For now we are just returning a string, but in real time we will be fetching the loans details from the DB
     * and returning the same to the user.
     *  */
    @GetMapping("/myLoans")
    public  String getLoansDetails () {
        return "Here are the loans details from the DB";
    }

}
