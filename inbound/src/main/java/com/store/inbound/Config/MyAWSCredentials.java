package com.store.inbound.Config;

import com.amazonaws.auth.AWSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
class MyAWSCredentials implements AWSCredentials {

    @Value("${cloud.aws.credentials.accessKey}")
    private String awsAccessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String awsSecretKey;

    @Override
    public String getAWSAccessKeyId() {
        return awsAccessKey;
    }

    @Override
    public String getAWSSecretKey() {
        return awsSecretKey;

    }
}