package com.store.orders;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="catalogueorder")
public class CatalogueOrder {
    private String username;
    private String isbn;
    private int copies;
    @Id
    @GeneratedValue
    private Long id;
    private boolean sent;

    public CatalogueOrder() {
    }

    public void setSent(boolean sent) { this.sent = sent; }

    public boolean isSent() { return sent; }

    public CatalogueOrder(String username, String isbn, int copies, boolean sent) {
        this.username = username;
        this.isbn = isbn;
        this.copies = copies;
        this.sent = sent;
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
    public String toString() {
        return "{ Username : " + this.username + " ISBN : " + this.isbn + " COPIES : " + this.copies + " ID " + this.id + " }";
    }

    public Map<String, Object> toJSON(){
        Map<String, Object> map = new HashMap<>();
        map.put("username",username);
        map.put("isbn",isbn);
        map.put("copies",String.valueOf(copies));
        return map;
    }

    public HttpEntity<Map<String, Object>> toRequestEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Map<String, Object> map = this.toJSON();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        return entity;
    }
}
