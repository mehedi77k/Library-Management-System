package com.librarymanagement.dto;

import com.librarymanagement.entity.BorrowRecord;

import java.math.BigDecimal;

public class OverdueReportRow {

    private BorrowRecord borrowRecord;
    private long overdueDays;
    private BigDecimal estimatedFine;

    public OverdueReportRow() {
    }

    public OverdueReportRow(BorrowRecord borrowRecord, long overdueDays, BigDecimal estimatedFine) {
        this.borrowRecord = borrowRecord;
        this.overdueDays = overdueDays;
        this.estimatedFine = estimatedFine;
    }

    public BorrowRecord getBorrowRecord() {
        return borrowRecord;
    }

    public long getOverdueDays() {
        return overdueDays;
    }

    public BigDecimal getEstimatedFine() {
        return estimatedFine;
    }

    public void setBorrowRecord(BorrowRecord borrowRecord) {
        this.borrowRecord = borrowRecord;
    }

    public void setOverdueDays(long overdueDays) {
        this.overdueDays = overdueDays;
    }

    public void setEstimatedFine(BigDecimal estimatedFine) {
        this.estimatedFine = estimatedFine;
    }
}