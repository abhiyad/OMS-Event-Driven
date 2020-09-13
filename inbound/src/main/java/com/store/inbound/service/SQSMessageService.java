package com.store.inbound.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.inbound.Config.MyAWSCredentialsProvider;
import com.store.inbound.domain.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;



@Component
public class SQSMessageService {

    @Value("${cloud.aws.end-point.uri}")
    private String sqsEndPoint;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Autowired
    private MyAWSCredentialsProvider myAWSCredentialsProvider;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ReceiveMessageRequest receiveMessageRequest;

    private Logger logger = LoggerFactory.getLogger(SQSMessageService.class);

    private final AmazonSQS sqs = AmazonSQSClientBuilder.standard().withRegion("us-east-2").withCredentials(myAWSCredentialsProvider).build();

    public void send(Book book) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(sqsEndPoint)
                .withMessageBody(book.toString())
                .withMessageGroupId("groupID")
                .withMessageDeduplicationId(String.valueOf(System.currentTimeMillis()));
        sqs.sendMessage(send_msg_request);
    }

}

