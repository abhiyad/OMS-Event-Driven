package com.store.orders;

import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

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

}
