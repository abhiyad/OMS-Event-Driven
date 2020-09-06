package com.store.gateway.domain;
import com.sun.istack.NotNull;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UserDAO")
public class UserDAO {

    @Id @NotNull
    private String username;
    @NotNull
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserDAO() {
    }

    public UserDAO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
