package com.librarymanagement.controller;

import com.librarymanagement.entity.BorrowStatus;
import com.librarymanagement.entity.Member;
import com.librarymanagement.repository.BorrowRecordRepository;
import com.librarymanagement.repository.MemberRepository;
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
@RequestMapping("/members")
public class MemberController {

    private static final List<Integer> PAGE_SIZES = List.of(5, 10, 20, 50);

    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public MemberController(
            MemberRepository memberRepository,
            BorrowRecordRepository borrowRecordRepository
    ) {
        this.memberRepository = memberRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @GetMapping
    public String listMembers(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false, defaultValue = "ALL") String status,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            Model model
    ) {
        int validPage = Math.max(page, 0);
        int validSize = normalizePageSize(size);

        String cleanKeyword = normalizeText(keyword);
        String selectedStatus = normalizeStatus(status);
        Boolean activeStatus = resolveActiveStatus(selectedStatus);

        Pageable pageable = PageRequest.of(
                validPage,
                validSize,
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<Member> memberPage = memberRepository.searchMembers(
                cleanKeyword,
                activeStatus,
                pageable
        );

        model.addAttribute("memberPage", memberPage);
        model.addAttribute("members", memberPage.getContent());

        model.addAttribute("keyword", cleanKeyword == null ? "" : cleanKeyword);
        model.addAttribute("status", selectedStatus);

        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pageSize", validSize);
        model.addAttribute("currentPage", memberPage.getNumber());

        return "members/list";
    }

    @GetMapping("/add")
    public String showAddMemberForm(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("pageTitle", "Add New Member");

        return "members/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditMemberForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Member member = memberRepository.findById(id).orElse(null);

        if (member == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found.");
            return "redirect:/members";
        }

        model.addAttribute("member", member);
        model.addAttribute("pageTitle", "Edit Member");

        return "members/form";
    }

    @PostMapping("/save")
    public String saveMember(
            @Valid @ModelAttribute("member") Member member,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", member.getId() == null ? "Add New Member" : "Edit Member");
            return "members/form";
        }

        try {
            if (member.getId() != null) {
                Member existingMember = memberRepository.findById(member.getId()).orElse(null);

                if (existingMember != null) {
                    member.setCreatedAt(existingMember.getCreatedAt());
                }
            }

            memberRepository.save(member);
            redirectAttributes.addFlashAttribute("successMessage", "Member saved successfully.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Member could not be saved. Email may already exist."
            );
        }

        return "redirect:/members";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteMember(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        boolean hasIssuedBook = borrowRecordRepository.existsByMemberIdAndStatus(id, BorrowStatus.ISSUED);

        if (hasIssuedBook) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "This member cannot be deleted because they have an active borrowed book."
            );
            return "redirect:/members";
        }

        if (memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Member deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found.");
        }

        return "redirect:/members";
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

    private String normalizeStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return "ALL";
        }

        String value = status.trim().toUpperCase();

        if (value.equals("ACTIVE") || value.equals("INACTIVE")) {
            return value;
        }

        return "ALL";
    }

    private Boolean resolveActiveStatus(String status) {
        if ("ACTIVE".equals(status)) {
            return true;
        }

        if ("INACTIVE".equals(status)) {
            return false;
        }

        return null;
    }
}