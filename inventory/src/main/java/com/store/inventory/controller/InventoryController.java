package com.store.inventory.controller;
import com.store.inventory.domain.Book;
import com.store.inventory.domain.CatalogueOrder;
import com.store.inventory.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryController {

    @Autowired
    private BookService bookService;

    @GetMapping("/search/isbn/{isbn}")
    public Book search(@PathVariable("isbn")String isbn){
       return bookService.find(isbn);
    }

    @GetMapping("/list")
    public List<Book> listAll(){
        return bookService.listAll();
    }

    @PostMapping("/create/order")
    public void create(@RequestBody CatalogueOrder order){
        bookService.createOrder(order);
    }

    @PostMapping("/rollback/order")
    public void rollBack(@RequestBody CatalogueOrder order){
        bookService.rollBack(order);
    }

}
