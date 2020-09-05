package com.store.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

@EnableBinding({Sink.class})
public class ConsumerErrorHandler {
    private Logger logger = LoggerFactory.getLogger(ConsumerErrorHandler.class);

    @ServiceActivator(inputChannel = "CREATE_ORDER.group.errors")
    public void errorHandlerCreate(Message<Object> message) throws JsonProcessingException {
        logger.info("ERROR occurred, Now in error Handler class");
        logger.info(message.getPayload().toString());
        logger.info(message.getHeaders().toString());
        // NOW HANDLE THIS ERROR
    }

    @ServiceActivator(inputChannel = "UPDATE_ORDER.group.errors")
    public void errorHandlerUpdate(Message<Object> message) throws JsonProcessingException {
        logger.info("ERROR occurred, Now in error Handler class");
        logger.info(message.getPayload().toString());
        logger.info(message.getHeaders().toString());
        // NOW HANDLE THIS ERROR
    }

    @ServiceActivator(inputChannel = "SEND_ORDER.group.errors")
    public void errorHandlerSend(Message<Object> message) throws JsonProcessingException {
        logger.info("ERROR occurred, Now in error Handler class");
        logger.info(message.getPayload().toString());
        logger.info(message.getHeaders().toString());
        // NOW HANDLE THIS ERROR
    }

}
