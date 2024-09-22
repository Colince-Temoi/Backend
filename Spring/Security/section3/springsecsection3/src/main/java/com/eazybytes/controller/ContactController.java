package com.eazybytes.controller;

import java.sql.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.eazybytes.model.Contact;
import com.eazybytes.repository.ContactRepository;

@RestController
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    /* I am going to accept the contact inquiry details from my front end application
     *  Based on the contact inquiry details, I am going to save it in the database and return it back to the front end application.
    * */
    @PostMapping("/contact")
    public Contact saveContactInquiryDetails(@RequestBody Contact contact) {
        /* Here I am taking the responsibility of generating the PK manually
        *  If you go into the Contact entity class, you can see that I have not defined any automatic sequence number generation annotations
        * */
        contact.setContactId(getServiceReqNumber());
        contact.setCreateDt(new Date(System.currentTimeMillis()));
        return contactRepository.save(contact);
    }

    /* I am going to generate a random number for the service request number
     * This is prefixed with SR and followed by a random number. SR stands for Service Request
     * This is a unique number for each contact inquiry
     * */
    public String getServiceReqNumber() {
        Random random = new Random();
        int ranNum = random.nextInt(999999999 - 9999) + 9999;
        return "SR"+ranNum;
    }
}