package com.store.gateway.repository;

import com.store.gateway.domain.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDAO,String> {
    UserDAO findByUsername(String username);
}
