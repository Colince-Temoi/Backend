package com.eazybytes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardsController {

    /* For now we are just returning a string, but in real time we will be fetching the card details from the DB
     * and returning the same to the user.
     *  */
    @GetMapping("/myCards")
    public  String getCardsDetails () {
        return "Here are the card details from the DB";
    }

}
