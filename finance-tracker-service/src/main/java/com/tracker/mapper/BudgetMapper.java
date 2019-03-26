package com.tracker.mapper;

import com.tracker.domain.Budget;
import com.tracker.dto.budget.BudgetRequestModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    Budget convertToBudget(BudgetRequestModel budgetRequestModel);
}
