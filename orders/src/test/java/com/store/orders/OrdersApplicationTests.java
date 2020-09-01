package com.store.orders;

import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class OrdersApplicationTests {

	@Mock
	private OrderService orderService;

	@Test
	void saveTest() throws NotFoundException {
		CatalogueOrder order = new CatalogueOrder("user","isbn",1,false);

		InventoryService inventoryService = mock(InventoryService.class);
		OrderRepository orderRepository = mock(OrderRepository.class);
		when(inventoryService.searchInventory("isbn")).thenReturn(new Book("isbn",10));
		Mockito.doNothing().when(inventoryService).blockInventory(order);

		orderService = new OrderService(inventoryService,orderRepository);

		orderService.save(order);
		List<CatalogueOrder> orderList = orderService.findOrderForUser("user");
		orderList.stream().forEach(o->System.out.println(o.toString()));
	}

}
