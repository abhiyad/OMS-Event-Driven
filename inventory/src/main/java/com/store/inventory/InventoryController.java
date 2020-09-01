package com.store.inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InventoryController {

    @Autowired
    private BookService bookService;

    @GetMapping("/search/isbn/{isbn}")
    public Book search(@PathVariable("isbn")String isbn){
        try{
            return bookService.find(isbn);
        } catch (Exception e) {
            throw new BookNotFoundException();
        }
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
        System.out.println("In ROLL BACK");
        bookService.rollBack(order);
    }

}
