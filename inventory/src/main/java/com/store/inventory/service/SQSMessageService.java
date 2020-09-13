package com.store.inventory.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.inventory.Config.MyAWSCredentialsProvider;
import com.store.inventory.domain.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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

    public List<Book> receive() {
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        messages.stream().forEach(m->sqs.deleteMessage(sqsEndPoint,m.getReceiptHandle()));
        List<Book>books = messages.stream().collect( Collectors.mapping(m-> {
            try {
                Book book = mapper.readValue(m.getBody(),Book.class);
                System.out.println(book);
                return book;
            } catch (JsonProcessingException e) {
               logger.info("Could not add : " + m.getBody());
            }
            return null;
        }, Collectors.toList()));
        return books;
    }
}

