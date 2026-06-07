package com.librarymanagement.controller;

import com.librarymanagement.entity.BorrowRecord;
import com.librarymanagement.entity.BorrowStatus;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BorrowRecordRepository;
import com.librarymanagement.repository.MemberRepository;
import com.librarymanagement.service.LibraryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/borrow")
public class BorrowController {

    private static final List<Integer> PAGE_SIZES = List.of(5, 10, 20, 50);

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
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false, defaultValue = "ALL") String status,
            @RequestParam(value = "overdue", required = false, defaultValue = "false") boolean overdue,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            Model model
    ) {
        int validPage = Math.max(page, 0);
        int validSize = normalizePageSize(size);

        String cleanKeyword = normalizeText(keyword);
        String selectedStatus = normalizeBorrowStatus(status);
        BorrowStatus statusFilter = resolveBorrowStatus(selectedStatus);

        Pageable pageable = PageRequest.of(
                validPage,
                validSize,
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<BorrowRecord> recordPage = borrowRecordRepository.searchBorrowRecords(
                cleanKeyword,
                statusFilter,
                overdue,
                BorrowStatus.ISSUED,
                LocalDate.now(),
                pageable
        );

        model.addAttribute("recordPage", recordPage);
        model.addAttribute("records", recordPage.getContent());

        model.addAttribute("keyword", cleanKeyword == null ? "" : cleanKeyword);
        model.addAttribute("status", selectedStatus);
        model.addAttribute("overdue", overdue);

        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pageSize", validSize);
        model.addAttribute("currentPage", recordPage.getNumber());

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

    private int normalizePageSize(int size) {
        if (PAGE_SIZES.contains(size)) {
            return size;
        }

        return 10;
    }

    private String normalizeText(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value.trim();
    }

    private String normalizeBorrowStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return "ALL";
        }

        String value = status.trim().toUpperCase();

        if (value.equals("ISSUED") || value.equals("RETURNED")) {
            return value;
        }

        return "ALL";
    }

    private BorrowStatus resolveBorrowStatus(String status) {
        if ("ISSUED".equals(status)) {
            return BorrowStatus.ISSUED;
        }

        if ("RETURNED".equals(status)) {
            return BorrowStatus.RETURNED;
        }

        return null;
    }
}