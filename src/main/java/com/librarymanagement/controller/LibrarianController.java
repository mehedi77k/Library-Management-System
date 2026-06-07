package com.librarymanagement.controller;

import com.librarymanagement.dto.AppUserForm;
import com.librarymanagement.entity.AppUser;
import com.librarymanagement.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/librarians")
@PreAuthorize("hasRole('ADMIN')")
public class LibrarianController {

    private final AppUserService appUserService;

    public LibrarianController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public String listLibrarians(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model
    ) {
        List<AppUser> librarians = appUserService.searchLibrarians(keyword);

        model.addAttribute("librarians", librarians);
        model.addAttribute("keyword", keyword);

        return "librarians/list";
    }

    @GetMapping("/add")
    public String showAddLibrarianForm(Model model) {
        model.addAttribute("userForm", appUserService.createEmptyLibrarianForm());
        model.addAttribute("pageTitle", "Add New Librarian");
        model.addAttribute("editMode", false);

        return "librarians/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditLibrarianForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            AppUserForm form = appUserService.getLibrarianFormById(id);

            model.addAttribute("userForm", form);
            model.addAttribute("pageTitle", "Edit Librarian");
            model.addAttribute("editMode", true);

            return "librarians/form";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/librarians";
        }
    }

    @PostMapping("/save")
    public String saveLibrarian(
            @Valid @ModelAttribute("userForm") AppUserForm userForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        boolean editMode = userForm.getId() != null;

        if (!editMode && !StringUtils.hasText(userForm.getPassword())) {
            bindingResult.rejectValue(
                    "password",
                    "password.required",
                    "Password is required for a new librarian."
            );
        }

        if (StringUtils.hasText(userForm.getPassword()) && userForm.getPassword().trim().length() < 6) {
            bindingResult.rejectValue(
                    "password",
                    "password.length",
                    "Password must be at least 6 characters."
            );
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", editMode ? "Edit Librarian" : "Add New Librarian");
            model.addAttribute("editMode", editMode);
            return "librarians/form";
        }

        try {
            appUserService.saveLibrarian(userForm);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    editMode ? "Librarian updated successfully." : "Librarian created successfully."
            );

            return "redirect:/librarians";
        } catch (Exception exception) {
            bindingResult.reject("saveError", exception.getMessage());

            model.addAttribute("pageTitle", editMode ? "Edit Librarian" : "Add New Librarian");
            model.addAttribute("editMode", editMode);

            return "librarians/form";
        }
    }

    @PostMapping("/toggle-status/{id}")
    public String toggleLibrarianStatus(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            appUserService.toggleLibrarianStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Librarian status updated successfully.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/librarians";
    }

    @GetMapping("/reset-password/{id}")
    public String showResetPasswordPage(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            AppUser librarian = appUserService.getLibrarianById(id);

            model.addAttribute("librarian", librarian);

            return "librarians/reset-password";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/librarians";
        }
    }

    @PostMapping("/reset-password/{id}")
    public String resetPassword(
            @PathVariable Long id,
            @RequestParam String newPassword,
            RedirectAttributes redirectAttributes
    ) {
        try {
            appUserService.resetLibrarianPassword(id, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password reset successfully.");
            return "redirect:/librarians";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/librarians/reset-password/" + id;
        }
    }
}