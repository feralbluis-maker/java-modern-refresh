package org.example;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamExercises {
    public static void main(String[] args){
        List<Transaction> transactions = List.of(
                new Transaction("t1", new BigDecimal("2500.00"), "Salary", TransactionType.INCOME, LocalDate.of(2026, 11, 1)),
                new Transaction("t2", new BigDecimal("65.50"), "Groceries", TransactionType.EXPENSE, LocalDate.of(2026, 11, 3)),
                new Transaction("t3", new BigDecimal("120.00"), "Transport", TransactionType.EXPENSE, LocalDate.of(2026, 11, 5)),
                new Transaction("t4", new BigDecimal("45.20"), "Groceries", TransactionType.EXPENSE, LocalDate.of(2026, 11, 10))
        );
        // 1. Filtrar gastos grandes (> 50.00), ordenados de mayor a menor importe
        List<Transaction> largeExpenses = getLargeExpenses(transactions, new BigDecimal("50.00"));
        System.out.println("1. Large expenses (> 50.00): " + largeExpenses);

        // 2. Total de ingresos (INCOME)
        BigDecimal totalIncome = calculateTotalIncome(transactions);
        System.out.println("2. Total income: " + totalIncome);

        // 3. Categorías únicas en mayúscula y ordenadas alfabéticamente
        List<String> cleanCategories = getCleanCategories(transactions);
        System.out.println("3. Clean categories: " + cleanCategories);

        // 4. Agrupar gastos por categoría usando groupingBy y reducing
        Map<String, BigDecimal> expensesByCateory = groupExpensesByCategory(transactions);
        System.out.println("4. Expenses by category: " + expensesByCateory);

        // 5. Transacción de gasto de mayor importe
        Optional<Transaction> highestExpense = findHighestExpense(transactions);
        highestExpense.ifPresentOrElse(
                tx -> System.out.println("5. Highest expense: " + tx),
                () -> System.out.println("5. No expenses found")
        );
    }
    public static List<Transaction> getLargeExpenses(List<Transaction> transactions, BigDecimal minAmount) {
        return transactions.stream()
                .filter(tx -> tx.type() == TransactionType.EXPENSE)
                .filter(tx -> tx.amount().compareTo(minAmount) >0)
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .toList();
    }
    public static BigDecimal calculateTotalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(tx -> tx.type() == TransactionType.INCOME)
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static List<String> getCleanCategories(List<Transaction> transactions) {
        return transactions.stream()
                .map(tx -> tx.category().toUpperCase())
                .distinct()
                .sorted()
                .toList();
    }

    public static Map<String, BigDecimal> groupExpensesByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(tx -> tx.type() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::category,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::amount, BigDecimal::add)
                ));
    }

    public static Optional<Transaction> findHighestExpense(List<Transaction> transactions) {
        return transactions.stream()
                .filter(tx -> tx.type() == TransactionType.EXPENSE)
                .max(Comparator.comparing(Transaction::amount));
    }
}
