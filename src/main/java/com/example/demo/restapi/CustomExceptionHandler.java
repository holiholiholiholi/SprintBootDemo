package com.example.demo.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler  {
	private static final String MSG_NOT_FOUND = "The given person could not be found!";
	private static final String MSG_BAD_REQUEST = "Bad Request";
	
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason=MSG_NOT_FOUND)
	public void handlerNotFound() {
        log.error(MSG_NOT_FOUND);
    }
	
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason=MSG_BAD_REQUEST)
	public void handlerBadRequest() {
        log.error(MSG_BAD_REQUEST);
    }
}
