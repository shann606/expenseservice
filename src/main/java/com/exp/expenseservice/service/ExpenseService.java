package com.exp.expenseservice.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.exp.expenseservice.dto.CategoriesResponse;
import com.exp.expenseservice.dto.ExpenseRequest;
import com.exp.expenseservice.dto.ExpenseResponse;
import com.exp.expenseservice.dto.ExpenseSearch;
import com.exp.expenseservice.entity.AddtionalInfo;
import com.exp.expenseservice.entity.Expenses;
import com.exp.expenseservice.enums.PaymentType;
import com.exp.expenseservice.exception.ExpsenseNotFound;
import com.exp.expenseservice.mapper.ExpenseMapper;
import com.exp.expenseservice.repository.ExpenseRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class ExpenseService {

	private ExpenseRepository expenseRepo;
	private ExpenseMapper expenseMapper;
	private WebClient webClient;

	public ExpenseService(ExpenseRepository expenseRepo, ExpenseMapper expenseMapper, WebClient webClient) {
		this.expenseRepo = expenseRepo;
		this.expenseMapper = expenseMapper;
		this.webClient = webClient;

	}

	@Transactional(timeout = 10)
	public ExpenseResponse createExpense(ExpenseRequest request, String createdBy) {

		Expenses expenses = expenseMapper.toEntity(request);

		expenses.setInfo(
				AddtionalInfo.builder().createdby(createdBy).createdOn(OffsetDateTime.now(Clock.systemUTC())).build());

		Expenses result = expenseRepo.save(expenses);

		return expenseMapper.toDTO(result);
	}

	@Transactional(timeout = 10)
	public ExpenseResponse updateExpense(UUID id, ExpenseRequest request, String updatedBy) {

		Expenses expense = expenseRepo.findById(id)
				.orElseThrow(() -> new ExpsenseNotFound(id + " Not found in the system."));

		Optional.ofNullable(request.userid()).ifPresent(x -> expense.setUserId(request.userid()));
		Optional.ofNullable(request.categoryid()).ifPresent(x -> expense.setCategoryId(request.categoryid()));
		Optional.ofNullable(request.subcatgegoryid())
				.ifPresent(x -> expense.setSubCategoryId(request.subcatgegoryid()));
		Optional.ofNullable(request.amount()).ifPresent(x -> expense.setAmount(request.amount()));
		Optional.ofNullable(request.payment()).ifPresent(x -> expense.setPaymentType(request.payment()));
		Optional.ofNullable(request.comments()).ifPresent(x -> expense.setComments(request.comments()));
		Optional.ofNullable(request.spenton())
				.ifPresent(x -> expense.setSpentOn(expenseMapper.dateConverter(request.spenton())));

		AddtionalInfo info = expense.getInfo();
		info.setUpdatedby(updatedBy);
		info.setUpdatedOn(OffsetDateTime.now(Clock.systemUTC()));

		expense.setInfo(info);
		Expenses result = expenseRepo.save(expense);

		return expenseMapper.toDTO(result);
	}

	@Transactional(readOnly = true)
	public ExpenseResponse getExpense(UUID id) {
		Expenses expense = expenseRepo.findById(id)
				.orElseThrow(() -> new ExpsenseNotFound(id + " Not found in the system."));
		return expenseMapper.toDTO(expense);
	}

	public void delete(UUID id) {

		expenseRepo.findById(id).orElseThrow(() -> new ExpsenseNotFound(id + " Not found in the system."));

		expenseRepo.deleteById(id);
	}

	public Flux<CategoriesResponse> getCategories() {

		return webClient.get().uri("/api/v1/categories").accept(MediaType.APPLICATION_JSON).retrieve()
				.onStatus(status -> status.is4xxClientError(),
						response -> Mono.error(new RuntimeException("DownStream application is having some issue")))
				.onStatus(status -> status.is5xxServerError(),
						response -> Mono.error(new RuntimeException("DownStream application is having Server issue")))
				.bodyToFlux(CategoriesResponse.class)

		;
	}

	public Flux<CategoriesResponse> getSubCategories(UUID categoryId) {

		return webClient.get().uri("/api/v1/categories/{categoryId}/sub-categories", categoryId)
				.accept(MediaType.APPLICATION_JSON).retrieve()
				.onStatus(status -> status.is4xxClientError(),
						response -> Mono.error(new RuntimeException("DownStream application is having some issue")))
				.onStatus(status -> status.is5xxServerError(),
						response -> Mono.error(new RuntimeException("DownStream application is having Server issue")))
				.bodyToFlux(CategoriesResponse.class);

	}

	public Flux<CategoriesResponse> getSubCategories() {

		return webClient.get().uri("/api/v1/categories/subcategories").accept(MediaType.APPLICATION_JSON).retrieve()
				.onStatus(status -> status.is4xxClientError(),
						response -> Mono.error(new RuntimeException("DownStream application is having some issue")))
				.onStatus(status -> status.is5xxServerError(),
						response -> Mono.error(new RuntimeException("DownStream application is having Server issue")))
				.bodyToFlux(CategoriesResponse.class)

		;
	}

	public Mono<Page<ExpenseSearch>> expenseSearch(UUID userId, PaymentType name, String fromDate, String toDate,UUID categoryId,
			int pageNo) {

		Mono<List<CategoriesResponse>> categories = getCategories().collectList();

		Mono<List<CategoriesResponse>> subCategories = getSubCategories().collectList();

		return Mono.zip(categories, subCategories).flatMap(data -> {

			List<CategoriesResponse> categories1 = data.getT1();
			List<CategoriesResponse> subCategories1 = data.getT2();

			OffsetDateTime spentOnF = StringUtils.hasText(fromDate) ? dateConverter(fromDate) : null;

			OffsetDateTime spentOnT = StringUtils.hasText(toDate) ? dateConverter(toDate) : null;

			return Mono.fromCallable(
					() -> expenseRepo.searchExpenses(userId, name, spentOnF, spentOnT,categoryId, PageRequest.of(pageNo, 3)))
					.subscribeOn(Schedulers.boundedElastic()).map(result -> {
						doChange(categories1, subCategories1, result);

						return result;
					});
		});
	}

	private void doChange(List<CategoriesResponse> categories1, List<CategoriesResponse> subCategories1,
			Page<ExpenseSearch> result) {

		result.stream().forEach((x) -> {

			x.setCategoryname(categories1.stream().filter(y -> y.id().equals(x.getCatgoryid())).findFirst()
					.map(CategoriesResponse::name).orElse("Unknown"));
			x.setSubCategoryName(subCategories1.stream().filter(y -> y.id().equals(x.getSubcatgoryid())).findFirst()
					.map(CategoriesResponse::name).orElse("Unknown"));

		});

	}

	private OffsetDateTime dateConverter(String date) {

		LocalDate localDate = LocalDate.parse(date);

		ZoneId dbZone = ZoneId.of("Asia/Kolkata");
		ZoneOffset dbOffset = dbZone.getRules().getOffset(localDate.atStartOfDay());

		return localDate.atStartOfDay().atOffset(dbOffset);

	}

}
