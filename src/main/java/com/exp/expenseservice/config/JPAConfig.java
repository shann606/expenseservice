package com.exp.expenseservice.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories("com.exp.expenseservice.repository")
@EnableTransactionManagement
public class JPAConfig {

}
