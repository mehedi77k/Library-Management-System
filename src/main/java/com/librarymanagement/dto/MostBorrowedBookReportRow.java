package com.librarymanagement.dto;

public class MostBorrowedBookReportRow {

    private String title;
    private String author;
    private String isbn;
    private long borrowCount;

    public MostBorrowedBookReportRow() {
    }

    public MostBorrowedBookReportRow(String title, String author, String isbn, long borrowCount) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.borrowCount = borrowCount;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public long getBorrowCount() {
        return borrowCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setBorrowCount(long borrowCount) {
        this.borrowCount = borrowCount;
    }
}