package com.librarymanagement.repository;

import com.librarymanagement.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCase(
            String title,
            String author,
            String isbn
    );

    long countByAvailableCopiesGreaterThan(Integer amount);

    @Query("""
            SELECT b FROM Book b
            WHERE
            (
                :keyword IS NULL
                OR LOWER(COALESCE(b.title, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(COALESCE(b.author, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(COALESCE(b.isbn, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(COALESCE(b.category, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            AND (:category IS NULL OR b.category = :category)
            AND
            (
                :availableStatus IS NULL
                OR (:availableStatus = TRUE AND b.availableCopies > 0)
                OR (:availableStatus = FALSE AND b.availableCopies <= 0)
            )
            """)
    Page<Book> searchBooks(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("availableStatus") Boolean availableStatus,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT b.category FROM Book b
            WHERE b.category IS NOT NULL AND b.category <> ''
            ORDER BY b.category ASC
            """)
    List<String> findDistinctCategories();
}