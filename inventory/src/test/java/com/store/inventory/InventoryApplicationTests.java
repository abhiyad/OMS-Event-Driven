package com.store.inventory;

import com.store.inventory.domain.Book;
import com.store.inventory.domain.CatalogueOrder;
import com.store.inventory.exceptions.BookNotFoundException;
import com.store.inventory.repository.BookRepository;
import com.store.inventory.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.mockito.Mockito.*;


class InventoryApplicationTests {
	@Mock
	public BookRepository bookRepository;

	@InjectMocks
	private BookService bookService;


	@Test
	void consumeTest(){
		Book book = new Book("isbn",10);

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
		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(book));
		when(bookRepository.save(book)).thenReturn(book);

		bookService.createOrder(order);

		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(new Book("isbn", 10-4)));
		when(bookRepository.save(book)).thenReturn(book);

		Book actual = bookService.find("isbn");
		Book expected = new Book("isbn",6);

		assert(actual.toString().equals(expected.toString()));
	}

	@Test
	void createOrderTestInvalidBook(){
		CatalogueOrder orderInvalid  = new CatalogueOrder("user","isbn2",10);
		doThrow(BookNotFoundException.class).when(bookRepository).findById("isbn2");
		assertThrows(BookNotFoundException.class,()->bookService.createOrder(orderInvalid));
	}

	@Test
	void rollBackInventoryTestBookPresent(){
		Book book = new Book("isbn",2);
		CatalogueOrder order = new CatalogueOrder("user","isbn",2);
		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(new Book("isbn", 10)));
		when(bookRepository.save(book)).thenReturn(book);

		bookService.rollBack(order);
		when(bookRepository.findById("isbn")).thenReturn(java.util.Optional.of(new Book("isbn", 12)));

		Book actual = bookService.find("isbn");
		Book expected = new Book("isbn",12);

		assert(actual.toString().equals(expected.toString()));
	}

	@Test
	void rollBackInventoryTestBookAbsent(){
		CatalogueOrder order2 = new CatalogueOrder("user","isbn2",10);
		bookService.rollBack(order2);
		when(bookRepository.findById("isbn2")).thenReturn(java.util.Optional.of(new Book("isbn2", 10)));

		Book expected = new Book("isbn2",10);
		Book actual = bookService.find("isbn2");

		assert (expected.toString().equals(actual.toString()));
	}


}