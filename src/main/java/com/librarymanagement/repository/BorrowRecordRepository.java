package com.librarymanagement.repository;

import com.librarymanagement.entity.BorrowRecord;
import com.librarymanagement.entity.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findAllByOrderByIdDesc();

    List<BorrowRecord> findByStatusOrderByDueDateAsc(BorrowStatus status);

    long countByStatus(BorrowStatus status);

    long countByStatusAndDueDateBefore(BorrowStatus status, LocalDate date);

    boolean existsByBookIdAndStatus(Long bookId, BorrowStatus status);

    boolean existsByMemberIdAndStatus(Long memberId, BorrowStatus status);

    @EntityGraph(attributePaths = {"book", "member"})
    @Query(
            value = """
                    SELECT r FROM BorrowRecord r
                    WHERE
                    (
                        :keyword IS NULL
                        OR LOWER(COALESCE(r.book.title, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(COALESCE(r.book.isbn, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(COALESCE(r.member.name, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(COALESCE(r.member.email, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(COALESCE(r.member.phone, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    )
                    AND (:status IS NULL OR r.status = :status)
                    AND
                    (
                        :overdueOnly = FALSE
                        OR (r.status = :issuedStatus AND r.dueDate < :today)
                    )
                    """,
            countQuery = """
                    SELECT COUNT(r) FROM BorrowRecord r
                    WHERE
                    (
                        :keyword IS NULL
                        OR LOWER(COALESCE(r.book.title, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(COALESCE(r.book.isbn, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(COALESCE(r.member.name, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(COALESCE(r.member.email, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                        OR LOWER(COALESCE(r.member.phone, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    )
                    AND (:status IS NULL OR r.status = :status)
                    AND
                    (
                        :overdueOnly = FALSE
                        OR (r.status = :issuedStatus AND r.dueDate < :today)
                    )
                    """
    )
    Page<BorrowRecord> searchBorrowRecords(
            @Param("keyword") String keyword,
            @Param("status") BorrowStatus status,
            @Param("overdueOnly") boolean overdueOnly,
            @Param("issuedStatus") BorrowStatus issuedStatus,
            @Param("today") LocalDate today,
            Pageable pageable
    );
}