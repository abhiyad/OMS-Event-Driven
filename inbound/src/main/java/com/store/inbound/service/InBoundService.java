package com.store.inbound.service;

import com.store.inbound.domain.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class InBoundService {


    Logger logger = LoggerFactory.getLogger(InBoundService.class);

    public void publishEvent(Book book){
      // send to AWS queue !

        logger.info("INBOUND_SERVICE : Published Event to add Book : " + book.toString());
    }
}
