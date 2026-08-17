package com.exp.expenseservice.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.exp.expenseservice.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseSearch {
	private UUID expid;
	private UUID catgoryid;
	private String categoryname;
	private UUID subcatgoryid;
	private String subCategoryName;
	private OffsetDateTime spenton;
	private BigDecimal amount;
	private PaymentType payment;

}
