package com.librarymanagement.controller;

import com.librarymanagement.entity.BorrowRecord;
import com.librarymanagement.entity.BorrowStatus;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BorrowRecordRepository;
import com.librarymanagement.repository.MemberRepository;
import com.librarymanagement.service.LibraryService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/borrow")
public class BorrowController {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final LibraryService libraryService;

    public BorrowController(
            BookRepository bookRepository,
            MemberRepository memberRepository,
            BorrowRecordRepository borrowRecordRepository,
            LibraryService libraryService
    ) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.libraryService = libraryService;
    }

    @GetMapping("/issue")
    public String showIssueBookPage(Model model) {
        model.addAttribute("books", bookRepository.findAll(Sort.by(Sort.Direction.ASC, "title")));
        model.addAttribute("members", memberRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));

        return "borrow/issue";
    }

    @PostMapping("/issue")
    public String issueBook(
            @RequestParam Long bookId,
            @RequestParam Long memberId,
            @RequestParam(defaultValue = "14") int borrowingDays,
            RedirectAttributes redirectAttributes
    ) {
        try {
            libraryService.issueBook(bookId, memberId, borrowingDays);
            redirectAttributes.addFlashAttribute("successMessage", "Book issued successfully.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/borrow/issue";
    }

    @GetMapping("/records")
    public String showBorrowRecords(
            @RequestParam(value = "filter", required = false) String filter,
            Model model
    ) {
        List<BorrowRecord> records;

        if ("active".equalsIgnoreCase(filter)) {
            records = borrowRecordRepository.findByStatusOrderByDueDateAsc(BorrowStatus.ISSUED);
        } else {
            records = borrowRecordRepository.findAllByOrderByIdDesc();
        }

        model.addAttribute("records", records);
        model.addAttribute("filter", filter);

        return "borrow/records";
    }

    @PostMapping("/return/{recordId}")
    public String returnBook(
            @PathVariable Long recordId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            libraryService.returnBook(recordId);
            redirectAttributes.addFlashAttribute("successMessage", "Book returned successfully.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/borrow/records";
    }
}