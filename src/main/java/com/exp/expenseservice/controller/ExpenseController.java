package com.exp.expenseservice.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exp.expenseservice.dto.CategoriesResponse;
import com.exp.expenseservice.dto.ExpenseRequest;
import com.exp.expenseservice.dto.ExpenseResponse;
import com.exp.expenseservice.dto.ExpenseSearch;
import com.exp.expenseservice.enums.PaymentType;
import com.exp.expenseservice.service.ExpenseService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/expenses")
@RestController
@Slf4j
public class ExpenseController {

	private ExpenseService expenseService;

	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	@PostMapping
	public ResponseEntity<ExpenseResponse> createExpense(@RequestBody ExpenseRequest request,
			@RequestHeader(name = "X-Username") String createdBy) {

		log.info("are we getting the data" + createdBy);

		return new ResponseEntity<ExpenseResponse>(expenseService.createExpense(request, createdBy),
				HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<ExpenseResponse> createExpense(@PathVariable UUID id, @RequestBody ExpenseRequest request,
			@RequestHeader(name = "X-Username") String updatedBy) {

		log.info("are we getting the data" + updatedBy);

		return new ResponseEntity<ExpenseResponse>(expenseService.updateExpense(id, request, updatedBy), HttpStatus.OK);

	}

	@GetMapping("/{id}")
	public ResponseEntity<ExpenseResponse> getExpense(@PathVariable UUID id) {

		return new ResponseEntity<ExpenseResponse>(expenseService.getExpense(id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteExpense(@PathVariable UUID id) {

		expenseService.delete(id);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}

	@GetMapping("/categories")
	public ResponseEntity<Flux<CategoriesResponse>> getCategories() {

		return new ResponseEntity<Flux<CategoriesResponse>>(expenseService.getCategories(), HttpStatus.OK);

	}

	@GetMapping("/subcategories/{categoryId}")
	public ResponseEntity<Flux<CategoriesResponse>> getSubCategories(@PathVariable UUID categoryId) {

		return new ResponseEntity<Flux<CategoriesResponse>>(expenseService.getSubCategories(categoryId), HttpStatus.OK);

	}

	@GetMapping("/search/{userId}")
	public ResponseEntity< Mono<Page<ExpenseSearch>>> expenseSearch(@PathVariable UUID userId,
			@RequestParam(required = false) PaymentType payment, @RequestParam(required = false) String fromdate,
			@RequestParam(required = false) String todate,  @RequestParam(required = false) UUID categoryId, @RequestParam(required = false) int pageNo) {

		return new ResponseEntity< Mono<Page<ExpenseSearch>>>(
				expenseService.expenseSearch(userId, payment, fromdate, todate,categoryId, pageNo), HttpStatus.OK);

	}

}
