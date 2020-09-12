package com.store.inventory.controller;
import com.store.inventory.domain.Book;
import com.store.inventory.domain.CatalogueOrder;
import com.store.inventory.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private BookService bookService;

    @GetMapping("/api/search/{isbn}")
    public Book search(@PathVariable("isbn")String isbn){
       return bookService.find(isbn);
    }

    @GetMapping("/api/list")
    public List<Book> listAll(){
        return bookService.listAll();
    }

    @PostMapping("/api/create/order")
    public void create(@RequestBody CatalogueOrder order){
        bookService.createOrder(order);
    }

    @PostMapping("/api/rollback/order")
    public void rollBack(@RequestBody CatalogueOrder order){
        bookService.rollBack(order);
    }

}
