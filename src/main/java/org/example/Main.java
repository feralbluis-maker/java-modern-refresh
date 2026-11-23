package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Main {
    static void main(String[] args) {

        // 1. List: an ordered list of transactions(allows duplicates)
        List<Transaction> transactions = List.of(
                new Transaction("t1", new BigDecimal("2500.00"), "Salary", TransactionType.INCOME, LocalDate.of(2026,11, 1)),
                new Transaction("t2", new BigDecimal("65.50"), "Groceries", TransactionType.EXPENSE, LocalDate.of(2026, 11, 3)),
                new Transaction("t3", new BigDecimal("120.00"), "Transport", TransactionType.EXPENSE, LocalDate.of(2026, 11, 5)),
                new Transaction("t4", new BigDecimal("45.20"), "Groceries", TransactionType.EXPENSE, LocalDate.of(2026, 11, 10))
        );

        BigDecimal netBalance = calculateNetBalance(transactions);
        System.out.println("Net balance: " + netBalance);

        //2. SET: unique categories with no duplicates
        Set<String> uniqueCategories = new HashSet<>();
        for (Transaction tx : transactions) {
            uniqueCategories.add(tx.category());
        }
        System.out.println("Unique categories: " + uniqueCategories);

        // 3. MAP: calculate total spent by category (EXPENSE only)
        Map<String, BigDecimal> expensesByCategory = new HashMap<>();
        for (Transaction tx : transactions){
            if (tx.type() == TransactionType.EXPENSE) {
                expensesByCategory.merge(tx.category(), tx.amount(), BigDecimal::add);
            }
        }
        System.out.println("Expenses by category: " + expensesByCategory);

        // 4.OPTIONAL: Retrieve a transaction safely, without a NullPointerException
        Optional<Transaction> firstGroceries = findFirstByCategory(transactions, "Groceries");
        firstGroceries.ifPresentOrElse(
                tx -> System.out.println("First Groceries transaction: " + tx),
                () -> System.out.println("No transaction found for category: Groceries")
        );

        String missinMessage = findFirstByCategory(transactions, "t999")
                .map(tx -> "Found " + tx.category())
                .orElse("Transaction not found");
        System.out.println(missinMessage);
    }
    public static Optional<Transaction> findFirstByCategory(List<Transaction> transactions, String category){
        for(Transaction tx : transactions) {
            if (tx.category().equals(category)) {
                return Optional.of(tx);
            }
        }
        return Optional.empty();
    }
    public static BigDecimal calculateNetBalance(List<Transaction> transactions) {
        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction tx : transactions) {
            if (tx.type() == TransactionType.INCOME) {
                balance = balance.add(tx.amount());
            } else if (tx.type() == TransactionType.EXPENSE) {
                balance = balance.subtract(tx.amount());
            }
        }
        return balance;

    }

}
