package com.librarymanagement.controller;

import com.librarymanagement.service.ExportService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/reports/export")
@PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
public class ReportExportController {

    private final ExportService exportService;

    public ReportExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/summary/pdf")
    public void exportSummaryPdf(HttpServletResponse response) throws IOException, DocumentException {
        exportService.exportReportSummaryPdf(response);
    }

    @GetMapping("/overdue/pdf")
    public void exportOverduePdf(HttpServletResponse response) throws IOException, DocumentException {
        exportService.exportOverduePdf(response);
    }

    @GetMapping("/overdue/excel")
    public void exportOverdueExcel(HttpServletResponse response) throws IOException {
        exportService.exportOverdueExcel(response);
    }

    @GetMapping("/issued/excel")
    public void exportIssuedExcel(HttpServletResponse response) throws IOException {
        exportService.exportIssuedExcel(response);
    }

    @GetMapping("/returned/excel")
    public void exportReturnedExcel(HttpServletResponse response) throws IOException {
        exportService.exportReturnedExcel(response);
    }

    @GetMapping("/fines/pdf")
    public void exportFinesPdf(HttpServletResponse response) throws IOException, DocumentException {
        exportService.exportFinesPdf(response);
    }

    @GetMapping("/fines/excel")
    public void exportFinesExcel(HttpServletResponse response) throws IOException {
        exportService.exportFinesExcel(response);
    }

    @GetMapping("/member-history/excel")
    public void exportMemberHistoryExcel(
            @RequestParam Long memberId,
            HttpServletResponse response
    ) throws IOException {
        exportService.exportMemberHistoryExcel(response, memberId);
    }

    @GetMapping("/most-borrowed/excel")
    public void exportMostBorrowedExcel(HttpServletResponse response) throws IOException {
        exportService.exportMostBorrowedExcel(response);
    }
}