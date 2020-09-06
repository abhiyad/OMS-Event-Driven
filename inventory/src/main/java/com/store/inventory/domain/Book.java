package com.store.inventory.domain;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name="Book")
public class Book {
    @Id
    @NotNull
    private String isbn;
    @Min(value = 0)
    private int copies;

    public Book(){}

    public Book(String isbn, int copies) {
        this.isbn = isbn;
        this.copies = copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public int getCopies() {
        return copies;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString(){
        return "{ ISBN : " + this.isbn + " COPIES : " + this.copies + " }";
    }

}
