package com.tracker.common.util;

import com.tracker.domain.Budget;
import com.tracker.domain.Transaction;

import java.time.LocalDateTime;
import java.util.Objects;

public final class FinanceUtils {

    private FinanceUtils() {
        throw new IllegalStateException("This is Utility class.");
    }

    /**
     * Checks if given transaction is in budget range:
     * {@link #isBetweenDates(LocalDateTime, LocalDateTime, LocalDateTime)}
     * and if the category is the same.
     *
     * @param budget {@link Budget}
     * @param transaction {@link Transaction}
     * @return true if the transaction is in budget range, else false
     */
    public static boolean isTransactionInBudgetRange(Budget budget, Transaction transaction) {
        return FinanceUtils.isBetweenDates(transaction.getDate(), budget.getFromDate(), budget.getToDate()) &&
                Objects.equals(transaction.getCategory(), budget.getCategory());
    }

    /**
     * Checks if given date is between the fromDate and toDate
     *
     * @param givenDate date
     * @param fromDate from date
     * @param toDate to date
     * @return if the given date is between the fromDate and toDate
     */
    private static boolean isBetweenDates(LocalDateTime givenDate, LocalDateTime fromDate, LocalDateTime toDate) {
        return givenDate.isAfter(fromDate) && givenDate.isBefore(toDate);
    }
}
