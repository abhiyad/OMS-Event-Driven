package com.store.orders.service;

import com.store.orders.domain.Book;
import com.store.orders.domain.CatalogueOrder;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
@Service
public class InventoryService {

    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(InventoryService.class);

    public void blockInventory(CatalogueOrder order){
        HttpEntity<Map<String, Object>> entity = order.toRequestEntity();
        restTemplate.postForLocation("http://InventoryService/create/order", entity);
    }

    public void rollBackInventory(CatalogueOrder order){
        HttpEntity<Map<String, Object>> entity = order.toRequestEntity();
        restTemplate.postForLocation("http://InventoryService/rollback/order", entity);
    }


    public Book searchInventory(String isbn) throws NotFoundException {
        try{
            Book responseBook = restTemplate.getForObject("http://InventoryService/search/isbn/" + isbn, Book.class);
            logger.info("ORDER_SERVICE : Found the Book for the ISBN : " + isbn);
            return responseBook;
        } catch (RestClientException e) {
            logger.info("ORDER_SERVICE : Could Not Find the Book for the ISBN : " + isbn);
            throw new NotFoundException("Book Not found");
        }
    }
}
