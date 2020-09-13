package com.store.inventory.service;

import com.store.inventory.domain.Book;
import com.store.inventory.domain.CatalogueOrder;
import com.store.inventory.exceptions.BookNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    Book find(String isbn)throws BookNotFoundException;
    List<Book> listAll();
    void createOrder(CatalogueOrder order);
    void rollBack(CatalogueOrder order);
    int getPreviousCopies(String isbn);
    void deleteAll();
    void add();
}
