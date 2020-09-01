package com.store.orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public CatalogueOrder create(@RequestBody CatalogueOrder order){
        if(orderService.save(order))
            return order;
        else
            throw new OrderNotPlacedException();

    }

    @PostMapping("/update")
    public CatalogueOrder update(@RequestBody CatalogueOrder order){
        if(orderService.update(order))
            return order;
        else
            throw new OrderNotPlacedException();

    }


}
