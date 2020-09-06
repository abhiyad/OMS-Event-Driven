package com.store.gateway.service;

import com.store.gateway.domain.CatalogueOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableBinding
public class MessageSenderService {

    @Autowired
    private BinderAwareChannelResolver resolver;

    @Transactional
    public void sendMessage(final String TOPIC_NAME, CatalogueOrder order){
        final MessageChannel messageChannel = resolver.resolveDestination(TOPIC_NAME);
        messageChannel.send(MessageBuilder.withPayload(order).build());
    }
}
