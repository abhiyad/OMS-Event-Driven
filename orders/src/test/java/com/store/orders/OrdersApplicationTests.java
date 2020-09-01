package com.store.orders;

import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

import static org.mockito.Mockito.*;

@SpringBootTest
class OrdersApplicationTests {

	@Mock
	private InventoryService inventoryService;

	@Mock
	private OrderRepository repository;

	@InjectMocks
	private OrderService service;

	@Test
	void saveTestValidOrder() throws NotFoundException {
		CatalogueOrder order = new CatalogueOrder("user","isbn",1,false);

		//Mocking the inventoryService
		when(inventoryService.searchInventory("isbn")).thenReturn(new Book("isbn",10));
		Mockito.doNothing().when(inventoryService).blockInventory(order);

		//Mocking the repository
		when(repository.save(order)).thenReturn(order);
		when(repository.findAllOrderForUser("user")).thenReturn(Collections.singletonList(order));

		// Calling the function
		service.save(order);

		List<CatalogueOrder> actualOrders = service.findOrderForUser("user");
		List<CatalogueOrder> expectedOrder = Collections.singletonList(order);

		assert (actualOrders.equals(expectedOrder));
	}

	@Test
	void saveTestInValidOrder() throws NotFoundException {
		CatalogueOrder order = new CatalogueOrder("user","isbn",10,false);

		//Mocking the inventoryService
		when(inventoryService.searchInventory("isbn")).thenReturn(new Book("isbn",1));
		Mockito.doNothing().when(inventoryService).blockInventory(order);

		//Mocking the repository
		when(repository.save(order)).thenReturn(order);
		when(repository.findAllOrderForUser("user")).thenReturn(Collections.emptyList());

		// Calling the function
		service.save(order);

		List<CatalogueOrder> actual = service.findOrderForUser("user");
		List<CatalogueOrder> expected = Collections.emptyList();

		assert (actual.equals(expected));
	}

	@Test
	void updateTestValid() throws NotFoundException {
		CatalogueOrder newOrder = new CatalogueOrder("user","isbn_new",4,false);
		CatalogueOrder prevOrder = new CatalogueOrder("user","isbn_old",3,false);
		newOrder.setID(1L); prevOrder.setID(1L);

		when(inventoryService.searchInventory("isbn_new")).thenReturn(new Book("isbn_new",10));
		when(inventoryService.searchInventory("isbn_old")).thenReturn(new Book("isbn_old",20));
		when(repository.existsById(1L)).thenReturn(true);
		when(repository.findById(1L)).thenReturn(java.util.Optional.of(prevOrder));
		when(repository.save(newOrder)).thenReturn(newOrder);

		service.update(newOrder);
		when(repository.findById(1L)).thenReturn(java.util.Optional.of(newOrder));
		CatalogueOrder actual = service.findById(1L);
		assert(actual.toString().equals(newOrder.toString()));
	}

	@Test
	void updateTestInvalid() throws NotFoundException {
		CatalogueOrder newOrder = new CatalogueOrder("user","isbn_new",40,false);
		CatalogueOrder prevOrder = new CatalogueOrder("user","isbn_old",3,false);
		newOrder.setID(1L); prevOrder.setID(1L);

		when(inventoryService.searchInventory("isbn_new")).thenReturn(new Book("isbn_new",10));
		when(inventoryService.searchInventory("isbn_old")).thenReturn(new Book("isbn_old",20));
		when(repository.existsById(1L)).thenReturn(true);
		when(repository.findById(1L)).thenReturn(java.util.Optional.of(prevOrder));
		when(repository.save(newOrder)).thenReturn(newOrder);

		service.update(newOrder);
		when(repository.findById(1L)).thenReturn(java.util.Optional.of(prevOrder));
		CatalogueOrder actual = service.findById(1L);
		assert(actual.toString().equals(prevOrder.toString()));
	}

	@Test
	void sendOrderValidTest(){
		CatalogueOrder newOrder = new CatalogueOrder("user","isbn",3,false);
		CatalogueOrder prevOrder = new CatalogueOrder("user","isbn",3,false);
		newOrder.setID(1L); prevOrder.setID(1L);

		when(repository.existsById(1L)).thenReturn(true);
		when(repository.findById(1L)).thenReturn(java.util.Optional.of(prevOrder));
		when(repository.save(newOrder)).thenReturn(newOrder);

		service.sendOrder(newOrder);
		newOrder.setSent(true);
		when(repository.findById(1L)).thenReturn(java.util.Optional.of(newOrder));
		CatalogueOrder actual = service.findById(1L);
		assert(actual.toString().equals(newOrder.toString()));
	}

	@Test
	void sendOrderTestInvalidId(){
		CatalogueOrder newOrder = new CatalogueOrder("user","isbn",3,false);
		CatalogueOrder prevOrder = new CatalogueOrder("user","isbn",3,false);
		newOrder.setID(1L); prevOrder.setID(2L);

		when(repository.existsById(1L)).thenReturn(false);
		when(repository.existsById(2L)).thenReturn(true);
		when(repository.findById(2L)).thenReturn(java.util.Optional.of(prevOrder));
		when(repository.save(newOrder)).thenReturn(newOrder);

		service.sendOrder(newOrder);
		when(repository.findById(1L)).thenReturn(null);
		assertNull(service.findById(1L));
	}

	@Test
	void sendOrderTestInvalidData(){
		CatalogueOrder newOrder = new CatalogueOrder("user","isbn1",3,false);
		CatalogueOrder prevOrder = new CatalogueOrder("user","isbn2",3,false);
		newOrder.setID(1L); prevOrder.setID(1L);

		when(repository.existsById(1L)).thenReturn(true);
		when(repository.findById(1L)).thenReturn(java.util.Optional.of(prevOrder));
		when(repository.save(newOrder)).thenReturn(newOrder);

		service.sendOrder(newOrder);
		CatalogueOrder actual = service.findById(1L);
		assert(actual.toString().equals(prevOrder.toString()));
	}
}