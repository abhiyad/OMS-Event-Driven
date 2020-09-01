package com.store.gateway;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDAO,String> {
    UserDAO findByUsername(String username);
}
