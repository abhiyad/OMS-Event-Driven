package com.store.inventory.Config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyAWSCredentialsProvider implements AWSCredentialsProvider {

    @Autowired
    private MyAWSCredentials awsCredentials;

    @Override
    public AWSCredentials getCredentials() {
        return awsCredentials;
    }

    @Override
    public void refresh() {

    }
}
