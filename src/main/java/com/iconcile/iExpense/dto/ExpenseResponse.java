package com.iconcile.iExpense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponse {

    private Long id;
    private String vendor;
    private String category;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private boolean anomaly;
    private String anomalyReason;

    public ExpenseResponse(Long id, String vendor, String category,
                           BigDecimal amount, LocalDate date,
                           String description, boolean anomaly, String anomalyReason) {
        this.id = id;
        this.vendor = vendor;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.anomaly = anomaly;
        this.anomalyReason = anomalyReason;
    }

    public ExpenseResponse() {

    }

    public Long getId() { return id; }
    public String getVendor() { return vendor; }
    public String getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public boolean isAnomaly() { return anomaly; }
    public String getAnomalyReason() { return anomalyReason; }
}