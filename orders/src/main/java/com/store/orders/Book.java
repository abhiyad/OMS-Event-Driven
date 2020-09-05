package com.store.orders;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Book")
public class Book {
    @Id
    @NotNull
    private String isbn;
    private int copies;

    public Book() {
    }

    public int getCopies() {
        return copies;
    }

    public Book(String isbn, int copies) {
        this.isbn = isbn;
        this.copies = copies;
    }

    public String getIsbn() {
        return isbn;
    }


    @Override
    public String toString(){
        return "{ ISBN : " + this.isbn + " COPIES : " + this.copies + " }";
    }

}
