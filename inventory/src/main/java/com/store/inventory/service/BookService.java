package com.store.inventory.service;
import com.store.inventory.domain.Book;
import com.store.inventory.domain.CatalogueOrder;
import com.store.inventory.exceptions.BookNotFoundException;
import com.store.inventory.exceptions.OrderNotPlaceException;
import com.store.inventory.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    private Logger logger = LoggerFactory.getLogger(BookService.class);

    public void deleteAll(){
        repository.deleteAll();
    }

    @StreamListener("input")
    public void consume(Book book){
        book.setCopies(book.getCopies() + getPreviousCopies(book.getIsbn()));
        repository.save(book);
        logger.info("INVENTORY_SERVICE : Book added Successfully .. current status : " + book.toString() );
    }

    public Book find(String isbn)throws BookNotFoundException {
        logger.info("INVENTORY_SERVICE : Searched for : " + isbn);
        return repository.findById(isbn).orElseThrow(BookNotFoundException::new);
    }

    public List<Book> listAll(){
        logger.info("INVENTORY_SERVICE : List of books requested");
        return repository.findAll();
    }

    public void createOrder(CatalogueOrder order) {
        try {
            Book book = find(order.getIsbn());
            book.setCopies(book.getCopies() - order.getCopies());
            repository.save(book);
        }
        catch (BookNotFoundException e){
            logger.info("INVENTORY_SERVICE : Book not found for the order : " + order.toString() );
            throw new BookNotFoundException();
        }
        catch (Exception e){
            logger.info("INVENTORY_SERVICE : Insufficient copies for the order : " + order.toString() );
            throw new OrderNotPlaceException();
        }
    }

    public void rollBack(CatalogueOrder order) {
        try{
            Book book = find(order.getIsbn());
            book.setCopies(book.getCopies() + order.getCopies());
            repository.save(book);
        } catch (BookNotFoundException e) {
            repository.save(new Book(order.getIsbn(), order.getCopies()));
        }
        logger.info("INVENTORY_SERVICE : Rolled back the Inventory for the order : " + order.toString());
    }

    private int getPreviousCopies(String isbn){
        if(repository.existsById(isbn)){
            Book book = repository.findById(isbn).orElse(null);
            return book.getCopies();
        }
        return 0;
    }
}
