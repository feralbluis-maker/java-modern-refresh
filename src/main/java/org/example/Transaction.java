package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction (
        String id,
        BigDecimal amount,
        String category,
        TransactionType type,
        LocalDate date
){

}
