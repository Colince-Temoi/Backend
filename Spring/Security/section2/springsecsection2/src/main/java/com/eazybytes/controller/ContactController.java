package com.eazybytes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactController {

    /* For now we are just returning a string, but in real time we will be saving the inquiry details to the DB
     * and returning the same to the user.
     */
    @GetMapping("/contact")
    public  String saveContactInquiryDetails () {
        return "Inquiry details are saved to the DB";
    }

}
