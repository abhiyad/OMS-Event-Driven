package com.store.inbound.service;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.inbound.domain.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;


@Component
public class SQSMessageService {

    @Value("${cloud.aws.end-point.uri}")
    private String sqsEndPoint;


    @Autowired
    private ObjectMapper mapper;

    private Logger logger = LoggerFactory.getLogger(SQSMessageService.class);

    private final AmazonSQS sqs = AmazonSQSClientBuilder.standard().withRegion("us-east-2").withCredentials( new EnvironmentVariableCredentialsProvider() ).build();

    EnvironmentVariableCredentialsProvider e = new EnvironmentVariableCredentialsProvider();


    public void send(Book book) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(sqsEndPoint)
                .withMessageBody(book.toString())
                .withMessageGroupId("groupID")
                .withMessageDeduplicationId(String.valueOf(System.currentTimeMillis()));
        sqs.sendMessage(send_msg_request);
    }

}

