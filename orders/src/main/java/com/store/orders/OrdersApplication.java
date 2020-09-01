package com.store.orders;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OrdersApplication {

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}

	@Autowired
	private OrderRepository orderRepository;

	@Bean
	public OrderService getOrderService(){
		InventoryService inventoryService = new InventoryService();
		return new OrderService(inventoryService,orderRepository);
	}

	public static void main(String[] args) {
		SpringApplication.run(OrdersApplication.class, args);
	}

}
