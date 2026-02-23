package com.iconcile.iExpense.repository;

import com.iconcile.iExpense.dto.dashboard.MonthlyCategoryTotal;
import com.iconcile.iExpense.dto.dashboard.TopVendorSpending;
import com.iconcile.iExpense.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("""
            SELECT AVG(e.amount)
            FROM Expense e
            WHERE e.vendor.category.id = :categoryId
            """)
    Double findAverageAmountByCategoryId(Long categoryId);

    @Query(value = """
    SELECT 
        c.name AS category,
        EXTRACT(YEAR FROM e.date) AS year,
        EXTRACT(MONTH FROM e.date) AS month,
        SUM(e.amount) AS total
    FROM expenses e
    JOIN vendors v ON e.vendor_id = v.id
    JOIN categories c ON v.category_id = c.id
    GROUP BY c.name, year, month
    ORDER BY year, month
""", nativeQuery = true)
    List<Object[]> getMonthlyCategoryTotalsRaw();

    @Query("""
        SELECT new com.iconcile.iExpense.dto.dashboard.TopVendorSpending(
            v.name,
            SUM(e.amount)
        )
        FROM Expense e
        JOIN e.vendor v
        GROUP BY v.name
        ORDER BY SUM(e.amount) DESC
        """)
    List<TopVendorSpending> findTopVendorsBySpending(org.springframework.data.domain.Pageable pageable);

    @Query("""
       SELECT e
       FROM Expense e
       WHERE e.anomaly = true
       ORDER BY e.date DESC
       """)
    List<Expense> findAllAnomalies();

    long countByAnomalyTrue();

    boolean existsByVendor_IdAndAmountAndDateAndDescription(
            Long vendorId,
            java.math.BigDecimal amount,
            java.time.LocalDate date,
            String description
    );

    @Query("""
       SELECT COALESCE(SUM(e.amount),0)
       FROM Expense e
       WHERE e.vendor.category.name = :category
       """)
    java.math.BigDecimal getTotalByCategory(String category);

    List<Expense> findTop10ByOrderByDateDescIdDesc();
}