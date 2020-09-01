package com.store.inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private ExecutorService executor;

    private Logger logger = LoggerFactory.getLogger(BookService.class);

    public void deleteAll(){
        repository.deleteAll();
    }

    @StreamListener("input")
    public void consume(Book book){
        logger.info("INVENTORY_SERVICE : Attempting to add Book : " + book.toString());
        int count = getPreviousCopies(book.getIsbn());
        book.setCopies(book.getCopies() + count);
        repository.save(book);
        logger.info("INVENTORY_SERVICE : Book added Successfully .. current status : " + book.toString() );
    }

    public int getPreviousCopies(String isbn){
        if(repository.existsById(isbn)){
            Book book = repository.findById(isbn).orElse(null);
            return book.getCopies();
        }
        return 0;
    }

    public Book find(String isbn){
        if(repository.existsById(isbn)) {
            logger.info("INVENTORY_SERVICE : Successfully found the book for ISBN :" + isbn);
            return repository.findById(isbn).orElse(null);
        }
        else {
            logger.info("INVENTORY_SERVICE : Couldn't find the book for ISBN :" + isbn);
            throw new BookNotFoundException();
        }
    }

    public List<Book> listAll(){
        logger.info("INVENTORY_SERVICE : List of books requested");
        return repository.findAll();
    }

    public void createOrder(CatalogueOrder order){
        logger.info("INVENTORY_SERVICE : Blocking the inventory for the order :" + order.toString());
        if(repository.existsById(order.getIsbn())) {
            Book book = repository.findById(order.getIsbn()).orElse(null);
            if(book.getCopies() >= order.getCopies()) {
                book.setCopies(book.getCopies() - order.getCopies());
                repository.save(book);
                logger.info("INVENTORY_SERVICE : Blocked the inventory for the order :" + order.toString());
            }
        }
        else{
            logger.info("INVENTORY_SERVICE : Could Not block the inventory for the order : " + order.toString());
        }
    }

    public void rollBack(CatalogueOrder order) {
        logger.info("INVENTORY_SERVICE : Attempting to roll-back the Inventory for the order : " + order.toString());
        if(repository.existsById(order.getIsbn())) {
            Book book = repository.findById(order.getIsbn()).orElse(null);
            book.setCopies(book.getCopies() + order.getCopies());
            repository.save(book);
        }
        else{
            repository.save(new Book(order.getIsbn(), order.getCopies()));
        }
        logger.info("INVENTORY_SERVICE : Rolled back the Inventory for the order : " + order.toString());
    }
}
