package com.librarymanagement.dto;

import java.math.BigDecimal;

public class ReportSummary {

    private long totalBooks;
    private long availableBooks;
    private long totalMembers;
    private long activeMembers;
    private long issuedBooks;
    private long returnedBooks;
    private long overdueBooks;
    private BigDecimal totalFineCollected;

    public ReportSummary() {
    }

    public ReportSummary(
            long totalBooks,
            long availableBooks,
            long totalMembers,
            long activeMembers,
            long issuedBooks,
            long returnedBooks,
            long overdueBooks,
            BigDecimal totalFineCollected
    ) {
        this.totalBooks = totalBooks;
        this.availableBooks = availableBooks;
        this.totalMembers = totalMembers;
        this.activeMembers = activeMembers;
        this.issuedBooks = issuedBooks;
        this.returnedBooks = returnedBooks;
        this.overdueBooks = overdueBooks;
        this.totalFineCollected = totalFineCollected;
    }

    public long getTotalBooks() {
        return totalBooks;
    }

    public long getAvailableBooks() {
        return availableBooks;
    }

    public long getTotalMembers() {
        return totalMembers;
    }

    public long getActiveMembers() {
        return activeMembers;
    }

    public long getIssuedBooks() {
        return issuedBooks;
    }

    public long getReturnedBooks() {
        return returnedBooks;
    }

    public long getOverdueBooks() {
        return overdueBooks;
    }

    public BigDecimal getTotalFineCollected() {
        return totalFineCollected;
    }

    public void setTotalBooks(long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public void setAvailableBooks(long availableBooks) {
        this.availableBooks = availableBooks;
    }

    public void setTotalMembers(long totalMembers) {
        this.totalMembers = totalMembers;
    }

    public void setActiveMembers(long activeMembers) {
        this.activeMembers = activeMembers;
    }

    public void setIssuedBooks(long issuedBooks) {
        this.issuedBooks = issuedBooks;
    }

    public void setReturnedBooks(long returnedBooks) {
        this.returnedBooks = returnedBooks;
    }

    public void setOverdueBooks(long overdueBooks) {
        this.overdueBooks = overdueBooks;
    }

    public void setTotalFineCollected(BigDecimal totalFineCollected) {
        this.totalFineCollected = totalFineCollected;
    }
}