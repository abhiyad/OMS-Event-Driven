package com.store.inbound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InBoundController {

    @Autowired
    private InBoundService inBoundService;

    @PostMapping("/add")
    public Book addBook(@RequestBody Book book){
        inBoundService.publishEvent(book);
        return book;
    }
}
