package com.exp.expenseservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.exp.expenseservice.enums.PaymentType;

public record ExpenseRequest(UUID userid, UUID categoryid, UUID subcatgegoryid, BigDecimal amount, PaymentType payment, String comments,
		String spenton) {

}
