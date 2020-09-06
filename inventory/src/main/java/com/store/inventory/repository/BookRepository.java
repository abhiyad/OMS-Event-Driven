package com.store.inventory.repository;

import com.store.inventory.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,String> {
    List<Book> findAll();
    Optional<Book> findById(String isbn);
}
