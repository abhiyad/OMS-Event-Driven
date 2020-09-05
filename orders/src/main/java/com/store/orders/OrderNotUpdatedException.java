package com.store.orders;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Cannot update the order")
public class OrderNotUpdatedException extends RuntimeException{
}
