package com.store.gateway.controller;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.store.gateway.domain.Book;
import com.store.gateway.domain.CatalogueOrder;
import com.store.gateway.domain.UserDAO;
import com.store.gateway.service.AccountService;
import com.store.gateway.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class GatewayController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MyUserDetailService userDetailService;

    private String topicCreate = "CREATE_ORDER";
    private String topicUpdate = "UPDATE_ORDER";
    private String topicSend = "SEND_ORDER";

    @PostMapping("/signup")
    public UserDAO signup(@RequestBody UserDAO user){
        return userDetailService.save(user);
    }

    @PostMapping("/create")
    public String createOrder(@RequestBody CatalogueOrder order) {
        accountService.publishEvent(order, topicCreate);
        return "Event Created for Order creation";
    }

    @PostMapping("/update")
    public String updateOrder(@RequestBody CatalogueOrder order) {
        accountService.publishEvent(order,topicUpdate);
        return "Event Created for order Update";
    }

    @PostMapping("/send")
    public String sendOrder(@RequestBody CatalogueOrder order){
        order.setSent(true);
        accountService.publishEvent(order,topicSend);
        return "SENDING THE ORDER";
    }

    @GetMapping("/orders")
    public List<CatalogueOrder> getAllOrders(){
        return accountService.getAllOrders();
    }

    @GetMapping("/list")
    public List<Book> getAllBooks(){
        return accountService.getAllBooks();
    }


    @GetMapping("/search/{isbn}")
    public Book search(@PathVariable("isbn")String isbn){
       return accountService.search(isbn);
    }

    @GetMapping("/hello")
    public String hello(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        return "hello you passed the login :" + username;
    }


}
