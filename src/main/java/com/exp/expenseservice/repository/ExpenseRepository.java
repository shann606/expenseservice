package com.exp.expenseservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exp.expenseservice.entity.Expenses;

public interface ExpenseRepository extends JpaRepository<Expenses, UUID> {

}
