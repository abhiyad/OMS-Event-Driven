package com.store.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication

public class InventoryApplication {

	@Bean
	public ExecutorService getExecutors(){
		ExecutorService executor = Executors.newFixedThreadPool(5);
		return executor;
	}

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}

}
