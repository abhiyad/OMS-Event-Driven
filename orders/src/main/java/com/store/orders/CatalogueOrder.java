package com.store.orders;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="catalogueorder")
public class CatalogueOrder {
    private String username;
    private String isbn;
    @Min(value=0)
    private int copies;
    @Id
    @GeneratedValue
    private Long id;
    private boolean sent;

    public CatalogueOrder(String username, String isbn, int copies, boolean sent) {
        this.username = username;
        this.isbn = isbn;
        this.copies = copies;
        this.sent = sent;
    }

    public CatalogueOrder(){}

    public void setSent(boolean sent) { this.sent = sent; }

    public boolean isSent() { return sent; }

    public String getUsername() {
        return username;
    }

    public void setID(Long id){this.id = id;}

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

    public HttpEntity<Map<String, Object>> toRequestEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Map<String, Object> map = this.toJSON();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        return entity;
    }

    public boolean equalOrder(CatalogueOrder order){
        return (order.toString().equals(this.toString()));
    }

    private Map<String, Object> toJSON(){
        Map<String, Object> map = new HashMap<>();
        map.put("username",username);
        map.put("isbn",isbn);
        map.put("copies",String.valueOf(copies));
        return map;
    }
}
