package com.store.orders.service;

import com.store.orders.domain.Book;
import com.store.orders.domain.CatalogueOrder;
import feign.Headers;
import javassist.NotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("InventoryService")
public interface InventoryClient{

    @RequestMapping(value = "/inventory/api/create/order", method = RequestMethod.POST)
    @Headers("Content-Type: application/json")
    void blockInventory(@RequestBody CatalogueOrder order);

    @RequestMapping(value = "/inventory/api/rollback/order", method = RequestMethod.POST)
    @Headers("Content-Type: application/json")
    void rollBackInventory(@RequestBody CatalogueOrder order);

    @RequestMapping(value = "/inventory/api/search/{isbn}", method = RequestMethod.GET)
    Book searchInventory(@PathVariable("isbn") String isbn) throws NotFoundException;

    @RequestMapping(value = "/inventory/api/list")
    List<Book> getAll();
}
