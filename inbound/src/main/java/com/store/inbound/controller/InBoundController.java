package com.store.inbound.controller;

import com.store.inbound.domain.Book;
import com.store.inbound.service.SQSMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class InBoundController {
    private static final Logger logger = LoggerFactory.getLogger(InBoundController.class);

    @Autowired
    private SQSMessageService sqsMessageService;

    @PostMapping("/send")
    public void sendMessage(@RequestBody Book book) {
        logger.info("sending the message string");
        sqsMessageService.send(book);
    }

}
