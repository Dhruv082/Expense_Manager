package com.iconcile.iExpense.service.anomaly;

import com.iconcile.iExpense.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AnomalyDetectionService {

    private final ExpenseRepository expenseRepository;

    public AnomalyDetectionService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public boolean isAnomalous(Long categoryId, BigDecimal newAmount) {

        Double avg = expenseRepository.findAverageAmountByCategoryId(categoryId);


        if (avg == null || avg == 0) {
            return false;
        }

        BigDecimal average = BigDecimal.valueOf(avg);
        BigDecimal threshold = average.multiply(BigDecimal.valueOf(3));

        return newAmount.compareTo(threshold) > 0;
    }
}