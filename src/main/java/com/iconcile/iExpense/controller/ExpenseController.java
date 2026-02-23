package com.iconcile.iExpense.controller;

import com.iconcile.iExpense.dto.CreateExpenseRequest;
import com.iconcile.iExpense.dto.ExpenseResponse;
import com.iconcile.iExpense.dto.dashboard.TopVendorSpending;
import com.iconcile.iExpense.model.Expense;
import com.iconcile.iExpense.service.ExpenseService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.iconcile.iExpense.dto.dashboard.MonthlyCategoryTotal;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ExpenseResponse createExpense(@RequestBody CreateExpenseRequest request) {
        return expenseService.createExpense(request);
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/dashboard/top-vendors")
    public List<TopVendorSpending> getTopVendors() {
        return expenseService.getTop5Vendors();
    }

    @GetMapping("/dashboard/monthly-totals")
    public List<MonthlyCategoryTotal> getMonthlyTotals() {
        return expenseService.getMonthlyTotals();
    }

    @GetMapping("/dashboard/anomalies")
    public List<Expense> getAnomalies() {
        return expenseService.getAnomalies();
    }

    @GetMapping("/dashboard/anomalies/count")
    public long getAnomalyCount() {
        return expenseService.getAnomalyCount();
    }

    @PostMapping("/upload")
    public String uploadCSV(@RequestParam("file") MultipartFile file) {
        expenseService.processCSV(file);
        return "File processed successfully";
    }

    @GetMapping("/dashboard/category-total/{category}")
    public BigDecimal getCategoryTotal(@PathVariable String category) {
        return expenseService.getTotalForCategory(category);
    }

    @GetMapping("/recent")
    public List<Expense> getRecentExpenses() {
        return expenseService.getRecentExpenses();
    }

    @GetMapping("/health")
    public String health() {
        return "Expense Manager running";
    }
}