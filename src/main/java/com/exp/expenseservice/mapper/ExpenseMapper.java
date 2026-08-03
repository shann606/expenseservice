package com.exp.expenseservice.mapper;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import com.exp.expenseservice.dto.ExpenseRequest;
import com.exp.expenseservice.dto.ExpenseResponse;
import com.exp.expenseservice.entity.Expenses;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

	@Mappings({ @Mapping(source = "spenton", target = "spentOn", qualifiedByName = "dateConverter"),
			@Mapping(source = "userid", target = "userId"), @Mapping(source = "categoryid", target = "categoryId"),
			@Mapping(source = "subcatgegoryid", target = "subCategoryId"),
			@Mapping(source = "payment", target = "paymentType") })
	Expenses toEntity(ExpenseRequest req);

	@Mappings({ @Mapping(source = "spentOn", target = "spenton"), @Mapping(source = "userId", target = "userid"),
			@Mapping(source = "categoryId", target = "categoryid"),
			@Mapping(source = "subCategoryId", target = "subcatgegoryid"),
			@Mapping(source = "paymentType", target = "payment") })
	ExpenseResponse toDTO(Expenses ex);

	@Named("dateConverter")
	default OffsetDateTime dateConverter(String date) {

		LocalDate local = LocalDate.parse(date);

		ZoneId dbZone = ZoneId.of("Asia/Kolkata");
		ZoneOffset dbOffset = dbZone.getRules().getOffset(local.atStartOfDay());

		return local.atStartOfDay().atOffset(dbOffset);

	}

}
