package com.store.orders.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.orders.domain.CatalogueOrder;
import com.store.orders.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@EnableBinding(Sink.class)
public class OrderConsumer {

    @Autowired
    private OrderService orderService;

    private Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    @StreamListener(target = Sink.INPUT)
    public void consume(final Message<Object> message) throws JsonProcessingException {
        final String topic =  message.getHeaders().get("kafka_receivedTopic").toString();
        System.out.println(message.getPayload().toString());
        if(topic.equals("CREATE_ORDER")){
            CatalogueOrder order = getConsumedOrder(message);
            logger.info("ORDER_CONSUMER : CREATE_ORDER : " + order.toString());
            order.setSent(false);
            orderService.save(order);
        }
        else if(topic.equals("UPDATE_ORDER")){
            CatalogueOrder order = getConsumedOrder(message);
            logger.info("ORDER_CONSUMER : UPDATE_ORDER : " + order.toString());
            orderService.update(order);
        }
        else if(topic.equals(("SEND_ORDER"))){
            CatalogueOrder order = getConsumedOrder(message);
            logger.info("ORDER_CONSUMER : SEND_ORDER : " + order.toString());
            orderService.sendOrder(order);
        }
        else if (topic.equals(("myErrors"))){
            logger.info(" ========== >>><><>   error found ");
        }

    }

    private CatalogueOrder getConsumedOrder(final Message<Object> message) throws JsonProcessingException {
        String jsonString = (String) message.getPayload();
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(jsonString, Map.class);
        CatalogueOrder order = mapper.convertValue(map,CatalogueOrder.class);
        return order;
    }
}
