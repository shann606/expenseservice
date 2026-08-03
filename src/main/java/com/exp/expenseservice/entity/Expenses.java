package com.exp.expenseservice.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.exp.expenseservice.enums.PaymentType;

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

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "Exp_Transactions")
public class Expenses {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Column(name = "exp_userid", nullable = false)
	private UUID userId;
	@Column(name = "exp_category_id", nullable = false)
	private UUID categoryId;

	@Column(name = "exp_sub_category_id", nullable = false)
	private UUID subCategoryId;
	@Column(name="exp_amount", nullable=false)
	private BigDecimal amount;
	@Column(name="exp_payment_type")
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;
	@Column(name="exp_comments")
	private String comments;
	@Column(name="exp_spent_on")
	private OffsetDateTime spentOn;
	@Embedded
	private AddtionalInfo info;
	

}
