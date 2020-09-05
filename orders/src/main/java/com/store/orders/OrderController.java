package com.store.orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.POST;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders/{username}")
    public List<CatalogueOrder> listAllForUser(@PathVariable("username")String username){
        return orderService.findOrderForUser(username);
    }

    @PostMapping("/create")
    public void create(@RequestBody CatalogueOrder order){
        orderService.save(order);
    }

    @PostMapping("/update")
    public void update(@RequestBody CatalogueOrder order){
        orderService.update(order);
    }

    @PostMapping("/send")
    public void send(@RequestBody CatalogueOrder order){
        orderService.sendOrder(order);
    }


}
