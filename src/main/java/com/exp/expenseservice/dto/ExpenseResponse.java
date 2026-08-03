package com.exp.expenseservice.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.exp.expenseservice.enums.PaymentType;

/**
 * can be modified according to the req
 */

public record ExpenseResponse(UUID id, UUID userid, UUID categoryid, UUID subcatgegoryid, BigDecimal amount,
		PaymentType payment, String comments, OffsetDateTime spenton) {

}
