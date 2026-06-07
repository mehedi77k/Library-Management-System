package com.librarymanagement.controller;

import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.BorrowStatus;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.BorrowRecordRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private static final List<Integer> PAGE_SIZES = List.of(5, 10, 20, 50);

    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public BookController(
            BookRepository bookRepository,
            BorrowRecordRepository borrowRecordRepository
    ) {
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @GetMapping
    public String listBooks(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "availability", required = false, defaultValue = "ALL") String availability,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            Model model
    ) {
        int validPage = Math.max(page, 0);
        int validSize = normalizePageSize(size);

        String cleanKeyword = normalizeText(keyword);
        String cleanCategory = normalizeText(category);
        String selectedAvailability = normalizeAvailability(availability);
        Boolean availableStatus = resolveAvailableStatus(selectedAvailability);

        Pageable pageable = PageRequest.of(
                validPage,
                validSize,
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<Book> bookPage = bookRepository.searchBooks(
                cleanKeyword,
                cleanCategory,
                availableStatus,
                pageable
        );

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("books", bookPage.getContent());

        model.addAttribute("keyword", cleanKeyword == null ? "" : cleanKeyword);
        model.addAttribute("selectedCategory", cleanCategory == null ? "" : cleanCategory);
        model.addAttribute("availability", selectedAvailability);

        model.addAttribute("categories", bookRepository.findDistinctCategories());
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pageSize", validSize);
        model.addAttribute("currentPage", bookPage.getNumber());

        return "books/list";
    }

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("pageTitle", "Add New Book");

        return "books/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditBookForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Book not found.");
            return "redirect:/books";
        }

        model.addAttribute("book", book);
        model.addAttribute("pageTitle", "Edit Book");

        return "books/form";
    }

    @PostMapping("/save")
    public String saveBook(
            @Valid @ModelAttribute("book") Book book,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", book.getId() == null ? "Add New Book" : "Edit Book");
            return "books/form";
        }

        try {
            if (book.getId() == null) {
                book.setAvailableCopies(book.getTotalCopies());
            } else {
                Book existingBook = bookRepository.findById(book.getId()).orElse(null);

                if (existingBook != null) {
                    int borrowedCopies = existingBook.getTotalCopies() - existingBook.getAvailableCopies();

                    if (book.getTotalCopies() < borrowedCopies) {
                        bindingResult.rejectValue(
                                "totalCopies",
                                "invalid.totalCopies",
                                "Total copies cannot be less than currently issued copies: " + borrowedCopies
                        );

                        model.addAttribute("pageTitle", "Edit Book");
                        return "books/form";
                    }

                    book.setAvailableCopies(book.getTotalCopies() - borrowedCopies);
                    book.setCreatedAt(existingBook.getCreatedAt());
                } else {
                    book.setAvailableCopies(book.getTotalCopies());
                }
            }

            bookRepository.save(book);
            redirectAttributes.addFlashAttribute("successMessage", "Book saved successfully.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Book could not be saved. ISBN may already exist."
            );
        }

        return "redirect:/books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteBook(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        boolean hasIssuedRecord = borrowRecordRepository.existsByBookIdAndStatus(id, BorrowStatus.ISSUED);

        if (hasIssuedRecord) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "This book cannot be deleted because it has an active issued record."
            );
            return "redirect:/books";
        }

        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Book not found.");
        }

        return "redirect:/books";
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

    private String normalizeAvailability(String availability) {
        if (!StringUtils.hasText(availability)) {
            return "ALL";
        }

        String value = availability.trim().toUpperCase();

        if (value.equals("AVAILABLE") || value.equals("UNAVAILABLE")) {
            return value;
        }

        return "ALL";
    }

    private Boolean resolveAvailableStatus(String availability) {
        if ("AVAILABLE".equals(availability)) {
            return true;
        }

        if ("UNAVAILABLE".equals(availability)) {
            return false;
        }

        return null;
    }
}