package com.iconcile.iExpense.dto.dashboard;

import java.math.BigDecimal;

public class TopVendorSpending {

    private String vendorName;
    private BigDecimal total;

    public TopVendorSpending(String vendorName, BigDecimal total) {
        this.vendorName = vendorName;
        this.total = total;
    }

    public String getVendorName() {
        return vendorName;
    }

    public BigDecimal getTotal() {
        return total;
    }
}