package com.store.inbound.domain;


public class Book {

    private String isbn;
    private int copies;

    public Book() {
    }

    public void setCopies(int copies) {
        this.copies = copies;
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

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString(){
        return "{ ISBN : " + this.isbn + " COPIES : " + this.copies + " }";
    }

}
