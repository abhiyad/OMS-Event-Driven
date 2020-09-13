package com.store.inbound.Config;

import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class SQSConfig {
    @Value("${cloud.aws.end-point.uri}")
    private String sqsEndPoint;

    @Bean
    public ReceiveMessageRequest getReceiveMessageRequest() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsEndPoint);
        receiveMessageRequest.setMaxNumberOfMessages(10);
        receiveMessageRequest.setWaitTimeSeconds(20);
        return receiveMessageRequest;
    }
}
