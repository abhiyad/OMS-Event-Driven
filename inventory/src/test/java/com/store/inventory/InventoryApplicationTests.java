package com.store.inventory;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.mockito.Mockito.*;

@SpringBootTest
class InventoryApplicationTests {
	@Mock
	public BookRepository bookRepository;

	@InjectMocks
	private BookService bookService;

	@Test
	void findTest(){
		when(bookRepository.existsById("isbn")).thenReturn(true);
		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(new Book("isbn", 10)));

		Book actual = bookService.find("isbn");
		Book expected = new Book("isbn",10);

		assert (actual.toString().equals(expected.toString()));

		when(bookRepository.existsById("isbn2")).thenReturn(false);
		assertThrows(BookNotFoundException.class,()->bookService.find("isbn2"));
	}

	@Test
	void consumeTest(){
		Book book = new Book("isbn",10);

		when(bookRepository.existsById("isbn")).thenReturn(true);
		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(new Book("isbn", 12)));
		when(bookRepository.save(book)).thenReturn(book);

		bookService.consume(book);
		Book actual = bookService.find("isbn");
		Book expected = new Book("isbn",12);
		assert (actual.toString().equals(expected.toString()));
	}

	@Test
	void createOrderTestValid(){
		Book book = new Book("isbn",10);
		CatalogueOrder order = new CatalogueOrder("user","isbn",4);
		when(bookRepository.existsById("isbn")).thenReturn(true);
		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(new Book("isbn", 10)));
		when(bookRepository.save(book)).thenReturn(book);

		bookService.createOrder(order);

		when(bookRepository.existsById("isbn")).thenReturn(true);
		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(new Book("isbn", 10-4)));
		when(bookRepository.save(book)).thenReturn(book);

		Book actual = bookService.find("isbn");
		Book expected = new Book("isbn",6);

		assert(actual.toString().equals(expected.toString()));
	}

	@Test
	void createOrderTestInvalid(){
		Book book = new Book("isbn",10);
		CatalogueOrder order = new CatalogueOrder("user","isbn",40);
		when(bookRepository.existsById("isbn")).thenReturn(true);
		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(new Book("isbn", 10)));
		when(bookRepository.save(book)).thenReturn(book);

		bookService.createOrder(order);

		when(bookRepository.existsById("isbn")).thenReturn(true);
		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(new Book("isbn", 10)));

		Book actual = bookService.find("isbn");
		Book expected = new Book("isbn",10);

		assert(actual.toString().equals(expected.toString()));

		CatalogueOrder orderInvalid  = new CatalogueOrder("user","isbn2",10);
		when(bookRepository.existsById("isbn2")).thenReturn(false);
		bookService.createOrder(orderInvalid);

		assertThrows(BookNotFoundException.class,()->bookService.find("isbn2"));
	}

	@Test
	void rollBackInventoryTest(){
		Book book = new Book("isbn",2);
		CatalogueOrder order = new CatalogueOrder("user","isbn",2);
		when(bookRepository.existsById("isbn")).thenReturn(true);
		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(new Book("isbn", 10)));
		when(bookRepository.save(book)).thenReturn(book);

		bookService.rollBack(order);
		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(new Book("isbn", 12)));

		Book actual = bookService.find("isbn");
		Book expected = new Book("isbn",12);

		assert(actual.toString().equals(expected.toString()));

		CatalogueOrder order2 = new CatalogueOrder("user","isbn2",10);
		when(bookRepository.existsById("isbn2")).thenReturn(false);
		bookService.rollBack(order2);
		when(bookRepository.existsById("isbn2")).thenReturn(true);
		when(bookRepository.findById("isbn2")).thenReturn(java.util.Optional.of(new Book("isbn2", 10)));

		expected = new Book("isbn2",10);
		actual = bookService.find("isbn2");

		assert (expected.toString().equals(actual.toString()));

	}


}