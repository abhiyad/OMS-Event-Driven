package com.store.orders.controller;
import com.store.orders.domain.Book;
import com.store.orders.domain.CatalogueOrder;
import com.store.orders.service.OrderService;
import com.store.orders.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.POST;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping("/api/list/{username}")
    public List<CatalogueOrder> listAllForUser(@PathVariable("username")String username){
        return orderService.findOrderForUser(username);
    }

    @PostMapping("/api/create")
    public void create(@RequestBody CatalogueOrder order){
        orderService.save(order);
    }

    @PostMapping("/api/update")
    public void update(@RequestBody CatalogueOrder order){
        orderService.update(order);
    }

    @PostMapping("/api/send")
    public void send(@RequestBody CatalogueOrder order){
        orderService.sendOrder(order);
    }

    @GetMapping("/api/books")
    public List<Book> getAll(){ return orderService.getAllBooks();}


}
