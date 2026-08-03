package com.exp.expenseservice.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.exp.expenseservice.enums.BudgetType;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exp_budget")
public class ExpenseBudget {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Column(name = "exp_userid", nullable = false)
	private UUID userId;
	@Column(name = "exp_amount_budget")
	private BigDecimal budgetAmount;
	@Enumerated(EnumType.STRING)
	@Column(name = "exp_budget_type")
	private BudgetType budgetType;
	@Column(name = "exp_category_id")
	private UUID categoryId;

	@Embedded
	private AddtionalInfo info;

}
