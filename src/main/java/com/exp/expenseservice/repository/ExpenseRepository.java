package com.exp.expenseservice.repository;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.exp.expenseservice.dto.ExpenseSearch;
import com.exp.expenseservice.entity.Expenses;
import com.exp.expenseservice.enums.PaymentType;

public interface ExpenseRepository extends JpaRepository<Expenses, UUID> {

	@Query("""
		    SELECT new com.exp.expenseservice.dto.ExpenseSearch(
		        c.id,
		        c.categoryId,
		        "cname",
		        c.subCategoryId,
		        "scname",
		        c.spentOn,
		        c.amount,
		        c.paymentType
		    )
		    FROM Expenses c
		    WHERE c.userId = :userId
		      AND (:paymentType IS NULL OR c.paymentType = :paymentType)
		      AND (:fromDate IS NULL OR c.spentOn >= :fromDate)
		      AND (:toDate IS NULL OR c.spentOn <= :toDate)
		      AND (:categoryId IS NULL OR c.categoryId =:categoryId)
		    """)
		Page<ExpenseSearch> searchExpenses(
		    @Param("userId") UUID userId,
		    @Param("paymentType") PaymentType paymentType,
		    @Param("fromDate") OffsetDateTime fromDate,
		    @Param("toDate") OffsetDateTime toDate,
		    @Param("categoryId") UUID categoryId,
		    Pageable pageable
		);
	

}
