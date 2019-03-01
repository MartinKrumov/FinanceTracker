package com.tracker.rest;

import com.tracker.dto.budget.BudgetRequestModel;
import com.tracker.service.BudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BudgetResource {

    private final BudgetService budgetService;

    @PostMapping("users/{userId}/wallets/{walletId}/budgets")
    public ResponseEntity createWallet(@Valid @RequestBody BudgetRequestModel budgetRequestModel,
                                       @PathVariable Long userId,
                                       @PathVariable Long walletId) {
        log.info("Request for creating budget has been received with userId = [{}] and walletId = [{}]", userId, walletId);
        budgetService.createBudget(budgetRequestModel, userId, walletId);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
