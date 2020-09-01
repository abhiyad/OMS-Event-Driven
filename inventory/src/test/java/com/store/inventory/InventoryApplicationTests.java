package com.store.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class InventoryApplicationTests {

	@Autowired
	private BookService service;


	@Test
	void simpleAddTest() {
		service.deleteAll();
		Book book1 = new Book("isbn1", 10);
		Book book2 = new Book("isbn2", 10);
		service.consume(book1);
		service.consume(book2);
		assert(service.find("isbn1").toString().equals(book1.toString()));
		assert(service.find("isbn2").toString().equals(book2.toString()));
	}

	@Test
	void multipleAddTest() {
		service.deleteAll();
		service.consume(new Book("isbn1", 10));
		service.consume(new Book("isbn1", 10));
		service.consume(new Book("isbn2",10));
		Book actual = service.find("isbn1");
		Book expected = new Book("isbn1",20);
		assert(actual.toString().equals(expected.toString()));
	}

	@Test
	void blockInventoryValidBookTest(){
		service.deleteAll();
		service.consume(new Book("isbn1",10));
		CatalogueOrder order = new CatalogueOrder("user","isbn1",3);
		service.createOrder(order);
		Book actual = service.find("isbn1");
		Book expected = new Book("isbn1",7);
		assert (actual.toString().equals(expected.toString()));
	}

	@Test
	void blockInventoryInvalidBookTest(){
		service.deleteAll();
		service.consume(new Book("isbn1",10));
		CatalogueOrder order = new CatalogueOrder("user","isbn1",30);
		service.createOrder(order);
		Book actual = service.find("isbn1");
		Book expected = new Book("isbn1",10);
		assert (actual.toString().equals(expected.toString()));
	}

	@Test
	void rollBackInventoryTest(){
		service.deleteAll();
		CatalogueOrder rollBackOrder = new CatalogueOrder("User","isbn1",10);
		service.rollBack(rollBackOrder);
		Book actual = service.find("isbn1");
		Book expected = new Book("isbn1",10);
		assert (actual.toString().equals(expected.toString()));

		service.rollBack(rollBackOrder);
		actual = service.find("isbn1");
		expected = new Book("isbn1",20);
		assert (actual.toString().equals(expected.toString()));

	}

	@Test
	void searchTest(){
		service.deleteAll();
		service.consume(new Book("isbn1",10));
		assertThrows(BookNotFoundException.class,()->service.find("isbn2"));
		Book expected = service.find("isbn1");
		Book actual = new Book("isbn1",10);
		assert (expected.toString().equals(actual.toString()));
	}

}
