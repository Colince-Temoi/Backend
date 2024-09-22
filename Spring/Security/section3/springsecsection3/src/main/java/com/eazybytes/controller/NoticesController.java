package com.eazybytes.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybytes.model.Notice;
import com.eazybytes.repository.NoticeRepository;

@RestController
public class NoticesController {

    @Autowired
    private NoticeRepository noticeRepository;

    @GetMapping("/notices")
    public ResponseEntity<List<Notice>> getNotices() {
        List<Notice> notices = noticeRepository.findAllActiveNotices();
        if (notices != null ) {
            /*  I am going to return the list of notices to the front end application.
            * Setting the cache control headers to cache the response for 60 seconds
            * With this, what I am telling is: Whatever notices details I am sending; please use that for the next 60 seconds.
            * Which means, if a user is trying to reload my notices page withing 60 seconds, the REST API call will not happen for my UI application rather It will try to use the
            *  notices details that it has inside the cache. And this cache is going to be valid upto 60 seconds.
            * */
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                    .body(notices);
        }else {
            return null;
        }
    }

}