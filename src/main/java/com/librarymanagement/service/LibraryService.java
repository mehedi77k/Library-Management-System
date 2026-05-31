package com.librarymanagement.service;

import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.BorrowRecord;
import com.librarymanagement.entity.BorrowStatus;
import com.librarymanagement.entity.Member;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BorrowRecordRepository;
import com.librarymanagement.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class LibraryService {

    private static final BigDecimal DAILY_FINE_AMOUNT = new BigDecimal("10.00");

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public LibraryService(
            BookRepository bookRepository,
            MemberRepository memberRepository,
            BorrowRecordRepository borrowRecordRepository
    ) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Transactional
    public void issueBook(Long bookId, Long memberId, int borrowingDays) {
        if (borrowingDays <= 0) {
            throw new IllegalArgumentException("Borrowing days must be greater than 0.");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));

        if (!member.isActive()) {
            throw new IllegalStateException("This member is inactive and cannot borrow books.");
        }

        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("This book is currently not available.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(borrowingDays);

        BorrowRecord record = new BorrowRecord(book, member, issueDate, dueDate);

        bookRepository.save(book);
        borrowRecordRepository.save(record);
    }

    @Transactional
    public void returnBook(Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow record not found."));

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new IllegalStateException("This book has already been returned.");
        }

        LocalDate returnDate = LocalDate.now();

        record.setReturnDate(returnDate);
        record.setStatus(BorrowStatus.RETURNED);
        record.setFineAmount(calculateFine(record.getDueDate(), returnDate));

        Book book = record.getBook();

        int currentAvailableCopies = book.getAvailableCopies() == null ? 0 : book.getAvailableCopies();
        int totalCopies = book.getTotalCopies() == null ? 0 : book.getTotalCopies();

        if (currentAvailableCopies < totalCopies) {
            book.setAvailableCopies(currentAvailableCopies + 1);
        }

        bookRepository.save(book);
        borrowRecordRepository.save(record);
    }

    private BigDecimal calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isAfter(dueDate)) {
            long lateDays = ChronoUnit.DAYS.between(dueDate, returnDate);
            return DAILY_FINE_AMOUNT.multiply(BigDecimal.valueOf(lateDays));
        }

        return BigDecimal.ZERO;
    }
}