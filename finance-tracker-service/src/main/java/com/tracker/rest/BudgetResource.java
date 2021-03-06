package com.tracker.rest;

import com.tracker.domain.Budget;
import com.tracker.domain.Category;
import com.tracker.rest.dto.budget.BudgetCreationDTO;
import com.tracker.mapper.BudgetMapper;
import com.tracker.service.BudgetService;
import com.tracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Martin Krumov
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BudgetResource {

    private final BudgetMapper budgetMapper;
    private final BudgetService budgetService;
    private final CategoryService categoryService;

    @PostMapping("users/{userId}/wallets/{walletId}/budgets")
    public ResponseEntity createWallet(@Valid @RequestBody BudgetCreationDTO budgetDTO,
                                       @PathVariable Long userId,
                                       @PathVariable Long walletId) {
        log.info("Request for creating budget has been received with userId = [{}] and walletId = [{}]", userId, walletId);

        Category category = categoryService.findByIdOrThrow(budgetDTO.getCategoryId());

        Budget budget = budgetMapper.convertToBudget(budgetDTO);
        budget.setCategory(category);

        budgetService.createBudget(budget, userId, walletId);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
