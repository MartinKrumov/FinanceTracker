package com.tracker.mapper;

import com.tracker.domain.Budget;
import com.tracker.rest.dto.budget.BudgetCreationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    Budget convertToBudget(BudgetCreationDTO budgetCreationDTO);
}
