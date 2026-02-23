package com.iconcile.iExpense.dto.dashboard;

import java.math.BigDecimal;

public class MonthlyCategoryTotal {

    private String category;
    private Integer year;
    private Integer month;
    private BigDecimal total;

    public MonthlyCategoryTotal(String category, Number year, Number month, BigDecimal total) {
        this.category = category;
        this.year = year.intValue();
        this.month = month.intValue();
        this.total = total;
    }

    public String getCategory() { return category; }
    public Integer getYear() { return year; }
    public Integer getMonth() { return month; }
    public BigDecimal getTotal() { return total; }
}