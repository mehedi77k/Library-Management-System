package com.librarymanagement.controller;

import com.librarymanagement.entity.BorrowStatus;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BorrowRecordRepository;
import com.librarymanagement.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class DashboardController {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public DashboardController(
            BookRepository bookRepository,
            MemberRepository memberRepository,
            BorrowRecordRepository borrowRecordRepository
    ) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalBooks", bookRepository.count());
        model.addAttribute("availableBooks", bookRepository.countByAvailableCopiesGreaterThan(0));
        model.addAttribute("totalMembers", memberRepository.count());
        model.addAttribute("activeMembers", memberRepository.countByActiveTrue());
        model.addAttribute("issuedBooks", borrowRecordRepository.countByStatus(BorrowStatus.ISSUED));
        model.addAttribute("overdueBooks", borrowRecordRepository.countByStatusAndDueDateBefore(
                BorrowStatus.ISSUED,
                LocalDate.now()
        ));

        return "dashboard";
    }
}