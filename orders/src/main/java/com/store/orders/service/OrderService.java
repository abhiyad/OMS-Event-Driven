package com.store.orders.service;

import com.store.orders.domain.Book;
import com.store.orders.domain.CatalogueOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    CatalogueOrder findById(Long Id);
    void save(CatalogueOrder order);
    List<CatalogueOrder> findOrderForUser(String username);
    void update(CatalogueOrder order);
    void sendOrder(CatalogueOrder order);
    List<Book> getAllBooks();
}
