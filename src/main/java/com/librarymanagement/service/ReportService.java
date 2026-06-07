package com.librarymanagement.service;

import com.librarymanagement.dto.MostBorrowedBookReportRow;
import com.librarymanagement.dto.OverdueReportRow;
import com.librarymanagement.dto.ReportSummary;
import com.librarymanagement.entity.BorrowRecord;
import com.librarymanagement.entity.BorrowStatus;
import com.librarymanagement.entity.Member;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BorrowRecordRepository;
import com.librarymanagement.repository.MemberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReportService {

    private static final BigDecimal DAILY_FINE_AMOUNT = new BigDecimal("10.00");

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public ReportService(
            BookRepository bookRepository,
            MemberRepository memberRepository,
            BorrowRecordRepository borrowRecordRepository
    ) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public ReportSummary getReportSummary() {
        BigDecimal totalFine = borrowRecordRepository.calculateTotalFineByStatus(BorrowStatus.RETURNED);

        if (totalFine == null) {
            totalFine = BigDecimal.ZERO;
        }

        return new ReportSummary(
                bookRepository.count(),
                bookRepository.countByAvailableCopiesGreaterThan(0),
                memberRepository.count(),
                memberRepository.countByActiveTrue(),
                borrowRecordRepository.countByStatus(BorrowStatus.ISSUED),
                borrowRecordRepository.countByStatus(BorrowStatus.RETURNED),
                borrowRecordRepository.countByStatusAndDueDateBefore(BorrowStatus.ISSUED, LocalDate.now()),
                totalFine
        );
    }

    public List<OverdueReportRow> getOverdueReportRows() {
        LocalDate today = LocalDate.now();

        List<BorrowRecord> overdueRecords = borrowRecordRepository.findOverdueIssuedRecords(
                BorrowStatus.ISSUED,
                today
        );

        return overdueRecords.stream()
                .map(record -> {
                    long overdueDays = ChronoUnit.DAYS.between(record.getDueDate(), today);
                    BigDecimal estimatedFine = DAILY_FINE_AMOUNT.multiply(BigDecimal.valueOf(overdueDays));

                    return new OverdueReportRow(record, overdueDays, estimatedFine);
                })
                .toList();
    }

    public List<BorrowRecord> getIssuedRecords() {
        return borrowRecordRepository.findIssuedReportRecords(BorrowStatus.ISSUED);
    }

    public List<BorrowRecord> getReturnedRecords() {
        return borrowRecordRepository.findReturnedReportRecords(BorrowStatus.RETURNED);
    }

    public List<BorrowRecord> getFineRecords() {
        return borrowRecordRepository.findFineReportRecords(BorrowStatus.RETURNED);
    }

    public BigDecimal getTotalCollectedFine() {
        BigDecimal totalFine = borrowRecordRepository.calculateTotalFineByStatus(BorrowStatus.RETURNED);

        if (totalFine == null) {
            return BigDecimal.ZERO;
        }

        return totalFine;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Member getMemberById(Long memberId) {
        if (memberId == null) {
            return null;
        }

        return memberRepository.findById(memberId).orElse(null);
    }

    public List<BorrowRecord> getMemberBorrowingHistory(Long memberId) {
        if (memberId == null) {
            return List.of();
        }

        return borrowRecordRepository.findMemberBorrowingHistory(memberId);
    }

    public List<MostBorrowedBookReportRow> getMostBorrowedBooks(int limit) {
        int validLimit = Math.max(limit, 1);

        List<Object[]> rows = borrowRecordRepository.findMostBorrowedBookRows(
                PageRequest.of(0, validLimit)
        );

        return rows.stream()
                .map(row -> new MostBorrowedBookReportRow(
                        row[0] == null ? "-" : row[0].toString(),
                        row[1] == null ? "-" : row[1].toString(),
                        row[2] == null ? "-" : row[2].toString(),
                        ((Number) row[3]).longValue()
                ))
                .toList();
    }
}