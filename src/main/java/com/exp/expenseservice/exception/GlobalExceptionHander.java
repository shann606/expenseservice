package com.exp.expenseservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHander {

	private static final String failedStatus = "Failed";

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ExceptionResponse> systemException(Exception ex) {

		ExceptionResponse res = new ExceptionResponse(failedStatus, ex.getMessage());

		return new ResponseEntity<ExceptionResponse>(res, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	

}