package com.librarymanagement.controller;

import com.librarymanagement.entity.BorrowRecord;
import com.librarymanagement.entity.Member;
import com.librarymanagement.service.ReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reports")
@PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public String showReportsDashboard(Model model) {
        model.addAttribute("summary", reportService.getReportSummary());
        model.addAttribute("topBorrowedBooks", reportService.getMostBorrowedBooks(5));
        model.addAttribute("overdueRows", reportService.getOverdueReportRows());

        return "reports/dashboard";
    }

    @GetMapping("/overdue")
    public String showOverdueReport(Model model) {
        model.addAttribute("overdueRows", reportService.getOverdueReportRows());

        return "reports/overdue";
    }

    @GetMapping("/issued")
    public String showIssuedReport(Model model) {
        model.addAttribute("records", reportService.getIssuedRecords());

        return "reports/issued";
    }

    @GetMapping("/returned")
    public String showReturnedReport(Model model) {
        model.addAttribute("records", reportService.getReturnedRecords());

        return "reports/returned";
    }

    @GetMapping("/fines")
    public String showFineReport(Model model) {
        model.addAttribute("records", reportService.getFineRecords());
        model.addAttribute("totalFine", reportService.getTotalCollectedFine());

        return "reports/fines";
    }

    @GetMapping("/member-history")
    public String showMemberHistoryReport(
            @RequestParam(value = "memberId", required = false) Long memberId,
            Model model
    ) {
        List<Member> members = reportService.getAllMembers();
        Member selectedMember = reportService.getMemberById(memberId);
        List<BorrowRecord> records = reportService.getMemberBorrowingHistory(memberId);

        model.addAttribute("members", members);
        model.addAttribute("selectedMember", selectedMember);
        model.addAttribute("selectedMemberId", memberId);
        model.addAttribute("records", records);

        return "reports/member-history";
    }

    @GetMapping("/most-borrowed")
    public String showMostBorrowedBooksReport(Model model) {
        model.addAttribute("rows", reportService.getMostBorrowedBooks(20));

        return "reports/most-borrowed";
    }
}