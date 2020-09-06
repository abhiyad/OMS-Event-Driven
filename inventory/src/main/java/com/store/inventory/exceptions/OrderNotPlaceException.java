package com.store.inventory.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Insufficient number of copies for this book")
public class OrderNotPlaceException extends RuntimeException{
}
