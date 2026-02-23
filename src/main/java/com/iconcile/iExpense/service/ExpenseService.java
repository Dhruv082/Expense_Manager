package com.iconcile.iExpense.service;

import com.iconcile.iExpense.dto.CreateExpenseRequest;
import com.iconcile.iExpense.dto.ExpenseResponse;
import com.iconcile.iExpense.dto.dashboard.TopVendorSpending;
import com.iconcile.iExpense.model.Expense;
import com.iconcile.iExpense.model.Vendor;
import com.iconcile.iExpense.repository.ExpenseRepository;
import com.iconcile.iExpense.repository.VendorRepository;
import com.iconcile.iExpense.service.anomaly.AnomalyDetectionService;
import com.iconcile.iExpense.service.rule.CategorizationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import com.iconcile.iExpense.dto.dashboard.MonthlyCategoryTotal;


import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final VendorRepository vendorRepository;
    private final CategorizationService categorizationService;
    private final AnomalyDetectionService anomalyDetectionService;

    public ExpenseService(ExpenseRepository expenseRepository,
                          VendorRepository vendorRepository,
                          CategorizationService categorizationService,
                          AnomalyDetectionService anomalyDetectionService) {
        this.expenseRepository = expenseRepository;
        this.vendorRepository = vendorRepository;
        this.categorizationService = categorizationService;
        this.anomalyDetectionService = anomalyDetectionService;
    }

    @Transactional
    public ExpenseResponse createExpense(CreateExpenseRequest request)
    {

        Vendor vendor = vendorRepository
                .findByNameIgnoreCase(request.getVendorName())
                .orElseGet(() -> {
                    Vendor newVendor = new Vendor(request.getVendorName().trim());

                    newVendor.setCategory(
                            categorizationService.resolveCategoryForVendor(request.getVendorName())
                    );

                    return vendorRepository.save(newVendor);
                });


        boolean exists = expenseRepository.existsByVendor_IdAndAmountAndDateAndDescription(
                vendor.getId(),
                request.getAmount(),
                request.getDate(),
                request.getDescription()
        );

        if (exists) {
            return new ExpenseResponse(
                    null,
                    vendor.getName(),
                    vendor.getCategory().getName(),
                    request.getAmount(),
                    request.getDate(),
                    request.getDescription(),
                    false,
                    "Duplicate expense ignored"
            );
        }


        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setDescription(request.getDescription());
        expense.setVendor(vendor);


        boolean anomaly = anomalyDetectionService.isAnomalous(
                vendor.getCategory().getId(),
                request.getAmount()
        );
        expense.setAnomaly(anomaly);


        Expense savedExpense=expenseRepository.save(expense);

        Double avg = expenseRepository.findAverageAmountByCategoryId(vendor.getCategory().getId());

        String reason = null;

        if (anomaly && avg != null) {
            reason = request.getAmount() + " is greater than 3× category average (avg: " +
                    String.format("%.2f", avg) + ")";
        }

        return new ExpenseResponse(
                savedExpense.getId(),
                vendor.getName(),
                vendor.getCategory().getName(),
                savedExpense.getAmount(),
                savedExpense.getDate(),
                savedExpense.getDescription(),
                anomaly,
                reason
        );
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }



    public List<MonthlyCategoryTotal> getMonthlyTotals() {

        List<Object[]> rows = expenseRepository.getMonthlyCategoryTotalsRaw();

        return rows.stream()
                .map(r -> new MonthlyCategoryTotal(
                        (String) r[0],
                        (Number) r[1],
                        (Number) r[2],
                        (java.math.BigDecimal) r[3]
                ))
                .toList();
    }


    public List<TopVendorSpending> getTop5Vendors() {
        return expenseRepository.findTopVendorsBySpending(PageRequest.of(0,5));
    }


    public List<Expense> getAnomalies() {
        return expenseRepository.findAllAnomalies();
    }


    public long getAnomalyCount() {
        return expenseRepository.countByAnomalyTrue();
    }

    @Transactional
    public void processCSV(MultipartFile file) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVParser csvParser = new CSVParser(reader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())) {

            for (CSVRecord record : csvParser) {

                // Skip completely empty rows
                if (record.size() == 0 ||
                        record.stream().allMatch(value -> value == null || value.trim().isEmpty())) {
                    continue;
                }

                // Also skip if mandatory fields missing
                if (record.get("date").isBlank() || record.get("amount").isBlank() || record.get("vendorName").isBlank()) {
                    continue;
                }

                CreateExpenseRequest request = new CreateExpenseRequest();
                request.setDate(LocalDate.parse(record.get("date"), formatter));
                request.setAmount(new BigDecimal(record.get("amount")));
                request.setVendorName(record.get("vendorName"));
                request.setDescription(record.get("description"));

                createExpense(request);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }

    public BigDecimal getTotalForCategory(String category) {
        return expenseRepository.getTotalByCategory(category);
    }

    public List<Expense> getRecentExpenses() {
        return expenseRepository.findTop10ByOrderByDateDescIdDesc();
    }
}