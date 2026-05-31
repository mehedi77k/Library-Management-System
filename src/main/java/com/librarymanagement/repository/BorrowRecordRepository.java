package com.librarymanagement.repository;

import com.librarymanagement.entity.BorrowRecord;
import com.librarymanagement.entity.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findAllByOrderByIdDesc();

    List<BorrowRecord> findByStatusOrderByDueDateAsc(BorrowStatus status);

    long countByStatus(BorrowStatus status);

    long countByStatusAndDueDateBefore(BorrowStatus status, LocalDate date);

    boolean existsByBookIdAndStatus(Long bookId, BorrowStatus status);

    boolean existsByMemberIdAndStatus(Long memberId, BorrowStatus status);
}