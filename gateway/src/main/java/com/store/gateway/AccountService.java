package com.store.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MessageSenderService messageSenderService;

    private Logger logger = LoggerFactory.getLogger(AccountService.class);

    private String getUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        return username;
    }

    public List<CatalogueOrder> getAllOrders(){
        logger.info("ACCOUNT_SERVICE : Getting all order for User :"  + getUsername() );
        ResponseEntity<List<CatalogueOrder>> responseEntity = restTemplate.exchange(
                "http://OrderService/orders/" + getUsername(), HttpMethod.GET, null, new ParameterizedTypeReference<List<CatalogueOrder>>() {
                });
        return responseEntity.getBody();
    }

    public List<Book> getAllBooks(){
        logger.info("ACCOUNT_SERVICE : Getting book list for User :"  + getUsername() );
        ResponseEntity<List<Book>> responseEntity = restTemplate.exchange(
                "http://InventoryService/list", HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {
                });
        return responseEntity.getBody();
    }

    public Book search(String isbn){
        logger.info("ACCOUNT_SERVICE : Searching for the Book with ISBN : "  + isbn );
        Book responseBook = restTemplate.getForObject("http://InventoryService/search/isbn/" + isbn, Book.class);
        return  responseBook;
    }

    public void publishEvent(CatalogueOrder order, String topic){
        order.setUsername(getUsername());
        logger.info("ACCOUNT_SERVICE : Publishing to the EVENT : " + topic + " FOR THE ORDER : " + order.toString() );
        messageSenderService.sendMessage(topic,order);
    }
}
