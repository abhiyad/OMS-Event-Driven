package com.store.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class InboundApplication {

	@Bean
	public ObjectMapper getObjectMapper(){
		ObjectMapper mapper = new ObjectMapper();
		return mapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(InboundApplication.class, args);
	}

}
