package com.librarymanagement.controller;

import com.librarymanagement.entity.BorrowStatus;
import com.librarymanagement.entity.Member;
import com.librarymanagement.repository.BorrowRecordRepository;
import com.librarymanagement.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberController {

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
            Model model
    ) {
        List<Member> members;

        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchText = keyword.trim();
            members = memberRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                    searchText,
                    searchText,
                    searchText
            );
        } else {
            members = memberRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }

        model.addAttribute("members", members);
        model.addAttribute("keyword", keyword);

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
}