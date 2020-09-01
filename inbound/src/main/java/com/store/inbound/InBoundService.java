package com.store.inbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class InBoundService {
    @Autowired
    private MessageChannel output;

    Logger logger = LoggerFactory.getLogger(InBoundService.class);

    public void publishEvent(Book book){
        output.send(MessageBuilder.withPayload(book).build());
        logger.info("INBOUND_SERVICE : Published Event to add Book : " + book.toString());
    }
}
