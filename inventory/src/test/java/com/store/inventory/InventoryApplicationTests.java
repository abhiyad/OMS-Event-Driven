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


}