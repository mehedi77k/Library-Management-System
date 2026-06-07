package com.librarymanagement.service;

import com.librarymanagement.dto.MostBorrowedBookReportRow;
import com.librarymanagement.dto.OverdueReportRow;
import com.librarymanagement.dto.ReportSummary;
import com.librarymanagement.entity.BorrowRecord;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ReportService reportService;

    public ExportService(ReportService reportService) {
        this.reportService = reportService;
    }

    public void exportReportSummaryPdf(HttpServletResponse response) throws IOException, DocumentException {
        preparePdfResponse(response, "reports-summary.pdf");

        ReportSummary summary = reportService.getReportSummary();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        addPdfTitle(document, "Library Reports Summary");
        addPdfGeneratedTime(document);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[]{2.5f, 1.5f});

        addPdfHeaderCell(table, "Metric");
        addPdfHeaderCell(table, "Value");

        addPdfCell(table, "Total Books");
        addPdfCell(table, String.valueOf(summary.getTotalBooks()));

        addPdfCell(table, "Available Books");
        addPdfCell(table, String.valueOf(summary.getAvailableBooks()));

        addPdfCell(table, "Total Members");
        addPdfCell(table, String.valueOf(summary.getTotalMembers()));

        addPdfCell(table, "Active Members");
        addPdfCell(table, String.valueOf(summary.getActiveMembers()));

        addPdfCell(table, "Issued Books");
        addPdfCell(table, String.valueOf(summary.getIssuedBooks()));

        addPdfCell(table, "Returned Books");
        addPdfCell(table, String.valueOf(summary.getReturnedBooks()));

        addPdfCell(table, "Overdue Books");
        addPdfCell(table, String.valueOf(summary.getOverdueBooks()));

        addPdfCell(table, "Total Fine Collected");
        addPdfCell(table, formatCurrency(summary.getTotalFineCollected()));

        document.add(table);
        document.close();
    }

    public void exportOverduePdf(HttpServletResponse response) throws IOException, DocumentException {
        preparePdfResponse(response, "overdue-books-report.pdf");

        List<OverdueReportRow> rows = reportService.getOverdueReportRows();

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        addPdfTitle(document, "Overdue Books Report");
        addPdfGeneratedTime(document);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[]{1.1f, 2.5f, 2.2f, 1.5f, 1.5f, 1.3f, 1.6f});

        addPdfHeaderCell(table, "Record ID");
        addPdfHeaderCell(table, "Book");
        addPdfHeaderCell(table, "Member");
        addPdfHeaderCell(table, "Issue Date");
        addPdfHeaderCell(table, "Due Date");
        addPdfHeaderCell(table, "Days");
        addPdfHeaderCell(table, "Estimated Fine");

        for (OverdueReportRow row : rows) {
            BorrowRecord record = row.getBorrowRecord();

            addPdfCell(table, String.valueOf(record.getId()));
            addPdfCell(table, safeText(record.getBook().getTitle()));
            addPdfCell(table, safeText(record.getMember().getName()));
            addPdfCell(table, formatDate(record.getIssueDate()));
            addPdfCell(table, formatDate(record.getDueDate()));
            addPdfCell(table, String.valueOf(row.getOverdueDays()));
            addPdfCell(table, formatCurrency(row.getEstimatedFine()));
        }

        document.add(table);
        document.close();
    }

    public void exportFinesPdf(HttpServletResponse response) throws IOException, DocumentException {
        preparePdfResponse(response, "fine-collection-report.pdf");

        List<BorrowRecord> records = reportService.getFineRecords();
        BigDecimal totalFine = reportService.getTotalCollectedFine();

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        addPdfTitle(document, "Fine Collection Report");
        addPdfGeneratedTime(document);

        Paragraph totalParagraph = new Paragraph(
                "Total Fine Collected: " + formatCurrency(totalFine),
                new Font(Font.HELVETICA, 13, Font.BOLD)
        );
        totalParagraph.setSpacingBefore(10);
        totalParagraph.setSpacingAfter(10);
        document.add(totalParagraph);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[]{1.1f, 2.5f, 2.2f, 1.5f, 1.5f, 1.5f});

        addPdfHeaderCell(table, "Record ID");
        addPdfHeaderCell(table, "Book");
        addPdfHeaderCell(table, "Member");
        addPdfHeaderCell(table, "Due Date");
        addPdfHeaderCell(table, "Return Date");
        addPdfHeaderCell(table, "Fine");

        for (BorrowRecord record : records) {
            addPdfCell(table, String.valueOf(record.getId()));
            addPdfCell(table, safeText(record.getBook().getTitle()));
            addPdfCell(table, safeText(record.getMember().getName()));
            addPdfCell(table, formatDate(record.getDueDate()));
            addPdfCell(table, formatDate(record.getReturnDate()));
            addPdfCell(table, formatCurrency(record.getFineAmount()));
        }

        document.add(table);
        document.close();
    }

    public void exportOverdueExcel(HttpServletResponse response) throws IOException {
        prepareExcelResponse(response, "overdue-books-report.xlsx");

        List<OverdueReportRow> rows = reportService.getOverdueReportRows();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Overdue Books");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);

            Row header = sheet.createRow(0);

            createCell(header, 0, "Record ID", headerStyle);
            createCell(header, 1, "Book", headerStyle);
            createCell(header, 2, "ISBN", headerStyle);
            createCell(header, 3, "Member", headerStyle);
            createCell(header, 4, "Member Email", headerStyle);
            createCell(header, 5, "Issue Date", headerStyle);
            createCell(header, 6, "Due Date", headerStyle);
            createCell(header, 7, "Overdue Days", headerStyle);
            createCell(header, 8, "Estimated Fine", headerStyle);

            int rowIndex = 1;

            for (OverdueReportRow reportRow : rows) {
                BorrowRecord record = reportRow.getBorrowRecord();
                Row row = sheet.createRow(rowIndex++);

                createCell(row, 0, record.getId(), normalStyle);
                createCell(row, 1, safeText(record.getBook().getTitle()), normalStyle);
                createCell(row, 2, safeText(record.getBook().getIsbn()), normalStyle);
                createCell(row, 3, safeText(record.getMember().getName()), normalStyle);
                createCell(row, 4, safeText(record.getMember().getEmail()), normalStyle);
                createCell(row, 5, formatDate(record.getIssueDate()), normalStyle);
                createCell(row, 6, formatDate(record.getDueDate()), normalStyle);
                createCell(row, 7, reportRow.getOverdueDays(), normalStyle);
                createCell(row, 8, reportRow.getEstimatedFine().doubleValue(), normalStyle);
            }

            autosizeColumns(sheet, 9);
            workbook.write(response.getOutputStream());
        }
    }

    public void exportIssuedExcel(HttpServletResponse response) throws IOException {
        prepareExcelResponse(response, "issued-books-report.xlsx");

        List<BorrowRecord> records = reportService.getIssuedRecords();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Issued Books");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);

            Row header = sheet.createRow(0);

            createCell(header, 0, "Record ID", headerStyle);
            createCell(header, 1, "Book", headerStyle);
            createCell(header, 2, "ISBN", headerStyle);
            createCell(header, 3, "Member", headerStyle);
            createCell(header, 4, "Member Email", headerStyle);
            createCell(header, 5, "Issue Date", headerStyle);
            createCell(header, 6, "Due Date", headerStyle);
            createCell(header, 7, "Status", headerStyle);

            int rowIndex = 1;

            for (BorrowRecord record : records) {
                Row row = sheet.createRow(rowIndex++);

                createCell(row, 0, record.getId(), normalStyle);
                createCell(row, 1, safeText(record.getBook().getTitle()), normalStyle);
                createCell(row, 2, safeText(record.getBook().getIsbn()), normalStyle);
                createCell(row, 3, safeText(record.getMember().getName()), normalStyle);
                createCell(row, 4, safeText(record.getMember().getEmail()), normalStyle);
                createCell(row, 5, formatDate(record.getIssueDate()), normalStyle);
                createCell(row, 6, formatDate(record.getDueDate()), normalStyle);
                createCell(row, 7, record.getStatus().name(), normalStyle);
            }

            autosizeColumns(sheet, 8);
            workbook.write(response.getOutputStream());
        }
    }

    public void exportReturnedExcel(HttpServletResponse response) throws IOException {
        prepareExcelResponse(response, "returned-books-report.xlsx");

        List<BorrowRecord> records = reportService.getReturnedRecords();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Returned Books");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);

            Row header = sheet.createRow(0);

            createCell(header, 0, "Record ID", headerStyle);
            createCell(header, 1, "Book", headerStyle);
            createCell(header, 2, "ISBN", headerStyle);
            createCell(header, 3, "Member", headerStyle);
            createCell(header, 4, "Issue Date", headerStyle);
            createCell(header, 5, "Due Date", headerStyle);
            createCell(header, 6, "Return Date", headerStyle);
            createCell(header, 7, "Fine Amount", headerStyle);
            createCell(header, 8, "Status", headerStyle);

            int rowIndex = 1;

            for (BorrowRecord record : records) {
                Row row = sheet.createRow(rowIndex++);

                createCell(row, 0, record.getId(), normalStyle);
                createCell(row, 1, safeText(record.getBook().getTitle()), normalStyle);
                createCell(row, 2, safeText(record.getBook().getIsbn()), normalStyle);
                createCell(row, 3, safeText(record.getMember().getName()), normalStyle);
                createCell(row, 4, formatDate(record.getIssueDate()), normalStyle);
                createCell(row, 5, formatDate(record.getDueDate()), normalStyle);
                createCell(row, 6, formatDate(record.getReturnDate()), normalStyle);
                createCell(row, 7, record.getFineAmount() == null ? 0 : record.getFineAmount().doubleValue(), normalStyle);
                createCell(row, 8, record.getStatus().name(), normalStyle);
            }

            autosizeColumns(sheet, 9);
            workbook.write(response.getOutputStream());
        }
    }

    public void exportFinesExcel(HttpServletResponse response) throws IOException {
        prepareExcelResponse(response, "fine-collection-report.xlsx");

        List<BorrowRecord> records = reportService.getFineRecords();
        BigDecimal totalFine = reportService.getTotalCollectedFine();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Fines");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);

            Row titleRow = sheet.createRow(0);
            createCell(titleRow, 0, "Total Fine Collected", headerStyle);
            createCell(titleRow, 1, totalFine == null ? 0 : totalFine.doubleValue(), normalStyle);

            Row header = sheet.createRow(2);

            createCell(header, 0, "Record ID", headerStyle);
            createCell(header, 1, "Book", headerStyle);
            createCell(header, 2, "Member", headerStyle);
            createCell(header, 3, "Due Date", headerStyle);
            createCell(header, 4, "Return Date", headerStyle);
            createCell(header, 5, "Fine Amount", headerStyle);

            int rowIndex = 3;

            for (BorrowRecord record : records) {
                Row row = sheet.createRow(rowIndex++);

                createCell(row, 0, record.getId(), normalStyle);
                createCell(row, 1, safeText(record.getBook().getTitle()), normalStyle);
                createCell(row, 2, safeText(record.getMember().getName()), normalStyle);
                createCell(row, 3, formatDate(record.getDueDate()), normalStyle);
                createCell(row, 4, formatDate(record.getReturnDate()), normalStyle);
                createCell(row, 5, record.getFineAmount() == null ? 0 : record.getFineAmount().doubleValue(), normalStyle);
            }

            autosizeColumns(sheet, 6);
            workbook.write(response.getOutputStream());
        }
    }

    public void exportMemberHistoryExcel(HttpServletResponse response, Long memberId) throws IOException {
        prepareExcelResponse(response, "member-borrowing-history.xlsx");

        List<BorrowRecord> records = reportService.getMemberBorrowingHistory(memberId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Member History");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);

            Row header = sheet.createRow(0);

            createCell(header, 0, "Record ID", headerStyle);
            createCell(header, 1, "Book", headerStyle);
            createCell(header, 2, "ISBN", headerStyle);
            createCell(header, 3, "Member", headerStyle);
            createCell(header, 4, "Issue Date", headerStyle);
            createCell(header, 5, "Due Date", headerStyle);
            createCell(header, 6, "Return Date", headerStyle);
            createCell(header, 7, "Status", headerStyle);
            createCell(header, 8, "Fine Amount", headerStyle);

            int rowIndex = 1;

            for (BorrowRecord record : records) {
                Row row = sheet.createRow(rowIndex++);

                createCell(row, 0, record.getId(), normalStyle);
                createCell(row, 1, safeText(record.getBook().getTitle()), normalStyle);
                createCell(row, 2, safeText(record.getBook().getIsbn()), normalStyle);
                createCell(row, 3, safeText(record.getMember().getName()), normalStyle);
                createCell(row, 4, formatDate(record.getIssueDate()), normalStyle);
                createCell(row, 5, formatDate(record.getDueDate()), normalStyle);
                createCell(row, 6, formatDate(record.getReturnDate()), normalStyle);
                createCell(row, 7, record.getStatus().name(), normalStyle);
                createCell(row, 8, record.getFineAmount() == null ? 0 : record.getFineAmount().doubleValue(), normalStyle);
            }

            autosizeColumns(sheet, 9);
            workbook.write(response.getOutputStream());
        }
    }

    public void exportMostBorrowedExcel(HttpServletResponse response) throws IOException {
        prepareExcelResponse(response, "most-borrowed-books.xlsx");

        List<MostBorrowedBookReportRow> rows = reportService.getMostBorrowedBooks(50);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Most Borrowed");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);

            Row header = sheet.createRow(0);

            createCell(header, 0, "Rank", headerStyle);
            createCell(header, 1, "Book Title", headerStyle);
            createCell(header, 2, "Author", headerStyle);
            createCell(header, 3, "ISBN", headerStyle);
            createCell(header, 4, "Total Borrowed", headerStyle);

            int rowIndex = 1;
            int rank = 1;

            for (MostBorrowedBookReportRow reportRow : rows) {
                Row row = sheet.createRow(rowIndex++);

                createCell(row, 0, rank++, normalStyle);
                createCell(row, 1, safeText(reportRow.getTitle()), normalStyle);
                createCell(row, 2, safeText(reportRow.getAuthor()), normalStyle);
                createCell(row, 3, safeText(reportRow.getIsbn()), normalStyle);
                createCell(row, 4, reportRow.getBorrowCount(), normalStyle);
            }

            autosizeColumns(sheet, 5);
            workbook.write(response.getOutputStream());
        }
    }

    private void prepareExcelResponse(HttpServletResponse response, String filename) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    }

    private void preparePdfResponse(HttpServletResponse response, String filename) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());

        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private CellStyle createNormalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private void createCell(Row row, int columnIndex, String value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value == null ? "-" : value);
        cell.setCellStyle(style);
    }

    private void createCell(Row row, int columnIndex, long value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createCell(Row row, int columnIndex, double value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void autosizeColumns(Sheet sheet, int numberOfColumns) {
        for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }

    private void addPdfTitle(Document document, String title) throws DocumentException {
        Paragraph paragraph = new Paragraph(title, new Font(Font.HELVETICA, 18, Font.BOLD));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(8);
        document.add(paragraph);
    }

    private void addPdfGeneratedTime(Document document) throws DocumentException {
        Paragraph paragraph = new Paragraph(
                "Generated at: " + LocalDateTime.now().format(DATE_TIME_FORMATTER),
                new Font(Font.HELVETICA, 10, Font.NORMAL)
        );
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(10);
        document.add(paragraph);
    }

    private void addPdfHeaderCell(PdfPTable table, String value) {
        Font font = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);

        PdfPCell cell = new PdfPCell(new Phrase(value, font));
        cell.setBackgroundColor(new Color(37, 99, 235));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(cell);
    }

    private void addPdfCell(PdfPTable table, String value) {
        Font font = new Font(Font.HELVETICA, 9, Font.NORMAL);

        PdfPCell cell = new PdfPCell(new Phrase(value == null ? "-" : value, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(cell);
    }

    private String formatDate(LocalDate date) {
        if (date == null) {
            return "-";
        }

        return date.format(DATE_FORMATTER);
    }

    private String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "BDT 0.00";
        }

        return "BDT " + value.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private String safeText(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "-";
        }

        return value.trim();
    }
}