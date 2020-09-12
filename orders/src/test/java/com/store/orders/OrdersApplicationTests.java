package com.store.orders;

import com.store.orders.domain.Book;
import com.store.orders.domain.CatalogueOrder;
import com.store.orders.exceptions.OrderNotFoundException;
import com.store.orders.exceptions.OrderNotPlacedException;
import com.store.orders.exceptions.OrderNotUpdatedException;
import com.store.orders.repository.OrderRepository;
import com.store.orders.service.InventoryClient;
import com.store.orders.service.OrderService;
import com.store.orders.service.OrderServiceImpl;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class OrdersApplicationTests {

	private InventoryClient inventoryClient = mock(InventoryClient.class);
	private OrderRepository repository = mock (OrderRepository.class);
	private OrderService service = new OrderServiceImpl(inventoryClient,repository);

	@Test
	void saveTestValidOrder() throws NotFoundException {
		CatalogueOrder order = new CatalogueOrder("user","isbn",1,false);

		//Mocking the inventoryService
		when(inventoryClient.searchInventory("isbn")).thenReturn(new Book("isbn",10));
		Mockito.doNothing().when(inventoryClient).blockInventory(order);

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
		when(inventoryClient.searchInventory("isbn")).thenReturn(new Book("isbn",1));
		doThrow(RuntimeException.class).when(inventoryClient).blockInventory(order);

		//Mocking the repository
		when(repository.save(order)).thenReturn(order);

		// Calling the function
		assertThrows(OrderNotPlacedException.class,()->service.save(order));
	}

	@Test
	void updateTestValid() throws NotFoundException {
		CatalogueOrder newOrder = new CatalogueOrder("user","isbn_new",4,false);
		CatalogueOrder prevOrder = new CatalogueOrder("user","isbn_old",3,false);
		newOrder.setID(1L); prevOrder.setID(1L);

		when(inventoryClient.searchInventory("isbn_new")).thenReturn(new Book("isbn_new",10));
		when(inventoryClient.searchInventory("isbn_old")).thenReturn(new Book("isbn_old",20));
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

		when(inventoryClient.searchInventory("isbn_new")).thenReturn(new Book("isbn_new",10));
		when(inventoryClient.searchInventory("isbn_old")).thenReturn(new Book("isbn_old",20));
		doThrow(RuntimeException.class).when(inventoryClient).blockInventory(newOrder);
		when(repository.findById(1L)).thenReturn(java.util.Optional.of(prevOrder));
		when(repository.save(newOrder)).thenReturn(newOrder);

		assertThrows(OrderNotPlacedException.class,()->service.update(newOrder));
	}

	@Test
	void sendOrderValidTest(){
		CatalogueOrder newOrder = new CatalogueOrder("user","isbn",3,false);
		CatalogueOrder prevOrder = new CatalogueOrder("user","isbn",3,false);
		newOrder.setID(1L); prevOrder.setID(1L);

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

		doThrow(OrderNotFoundException.class).when(repository).findById(1L);
		when(repository.existsById(2L)).thenReturn(true);
		when(repository.findById(2L)).thenReturn(java.util.Optional.of(prevOrder));
		when(repository.save(newOrder)).thenReturn(newOrder);

		assertThrows(OrderNotUpdatedException.class,()->service.sendOrder(newOrder));
	}

	@Test
	void sendOrderTestInvalidData(){
		CatalogueOrder newOrder = new CatalogueOrder("user","isbn1",3,false);
		CatalogueOrder prevOrder = new CatalogueOrder("user","isbn2",3,false);
		newOrder.setID(1L); prevOrder.setID(1L);

		when(repository.findById(1L)).thenReturn(java.util.Optional.of(prevOrder));
		when(repository.save(newOrder)).thenReturn(newOrder);

		assertThrows(OrderNotUpdatedException.class,()->service.sendOrder(newOrder));
	}
}
