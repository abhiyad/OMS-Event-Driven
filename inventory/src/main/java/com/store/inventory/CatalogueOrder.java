package com.store.inventory;
public class CatalogueOrder {
    private String username;
    private String isbn;
    private int copies;

    private Long id;

    public CatalogueOrder() {
    }

    public CatalogueOrder(String username, String isbn, int copies) {
        this.username = username;
        this.isbn = isbn;
        this.copies = copies;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getCopies() {
        return copies;
    }

    @Override
    public String toString(){
        return "{ ISBN :" + this.isbn + " Username :" + this.username + " Copies :" + this.copies + " }";
    }
}
