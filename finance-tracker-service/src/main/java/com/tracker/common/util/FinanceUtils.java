package com.tracker.common.util;

import com.tracker.domain.Budget;
import com.tracker.domain.Transaction;
import com.tracker.domain.enums.TransactionType;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

public final class FinanceUtils {

    private FinanceUtils() {
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
     * Calculates the total amount of given transactions.
     * Sums all {@link TransactionType#INCOME} and from them subtract all {@link TransactionType#EXPENSE}
     *
     * @param transactions {@link Collection} of {@link Transaction}s
     * @return {@link BigDecimal} total amount
     */
    public static BigDecimal calculateAdjustedAmount(Collection<Transaction> transactions) {
        if (CollectionUtils.isEmpty(transactions)) {
            return BigDecimal.ZERO;
        }

        Map<TransactionType, BigDecimal> transactionTypeSum = transactions.stream()
                .collect(groupingBy(Transaction::getType,
                        reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        BigDecimal totalAmount = BigDecimal.ZERO;

        if (transactionTypeSum.size() == 2) {
            totalAmount = transactionTypeSum.get(TransactionType.INCOME).subtract(transactionTypeSum.get(TransactionType.EXPENSE));
        } else if (transactionTypeSum.size() == 1) {

            TransactionType key = (TransactionType) transactionTypeSum.keySet().toArray()[0];
            if (key.equals(TransactionType.EXPENSE)) {
                totalAmount = totalAmount.subtract(transactionTypeSum.get(key));
            } else {
                totalAmount = transactionTypeSum.get(key);
            }
        }

        return totalAmount;
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
