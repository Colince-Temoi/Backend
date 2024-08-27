package com.eazybytes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticesController {

    /* For now we are just returning a string, but in real time we will be fetching the notices details from the DB
     * and returning the same to the user.
     *  */
    @GetMapping("/notices")
    public  String getNotices () {
        return "Here are the notices details from the DB";
    }

}
