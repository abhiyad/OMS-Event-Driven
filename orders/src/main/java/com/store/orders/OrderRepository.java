package com.store.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<CatalogueOrder,Long> {
    @Query(value = "Select * from catalogueorder where username = :username", nativeQuery = true)
    List<CatalogueOrder> findAllOrderForUser( @Param("username") String username);

}
