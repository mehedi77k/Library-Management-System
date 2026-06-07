package com.librarymanagement.repository;

import com.librarymanagement.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
            String name,
            String email,
            String phone
    );

    long countByActiveTrue();

    @Query("""
            SELECT m FROM Member m
            WHERE
            (
                :keyword IS NULL
                OR LOWER(COALESCE(m.name, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(COALESCE(m.email, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(COALESCE(m.phone, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(COALESCE(m.address, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            AND (:activeStatus IS NULL OR m.active = :activeStatus)
            """)
    Page<Member> searchMembers(
            @Param("keyword") String keyword,
            @Param("activeStatus") Boolean activeStatus,
            Pageable pageable
    );
}