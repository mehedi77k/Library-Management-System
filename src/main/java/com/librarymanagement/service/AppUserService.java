package com.librarymanagement.service;

import com.librarymanagement.dto.AppUserForm;
import com.librarymanagement.entity.AppUser;
import com.librarymanagement.entity.Role;
import com.librarymanagement.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUser> searchLibrarians(String keyword) {
        List<AppUser> librarians = appUserRepository.findByRoleOrderByIdDesc(Role.ROLE_LIBRARIAN);

        if (!StringUtils.hasText(keyword)) {
            return librarians;
        }

        String searchText = keyword.trim().toLowerCase(Locale.ROOT);

        return librarians.stream()
                .filter(user ->
                        containsIgnoreCase(user.getUsername(), searchText)
                                || containsIgnoreCase(user.getFullName(), searchText)
                                || containsIgnoreCase(user.getEmail(), searchText)
                )
                .toList();
    }

    public AppUserForm createEmptyLibrarianForm() {
        AppUserForm form = new AppUserForm();
        form.setEnabled(true);
        return form;
    }

    public AppUserForm getLibrarianFormById(Long id) {
        AppUser librarian = findLibrarianEntityById(id);

        return new AppUserForm(
                librarian.getId(),
                librarian.getUsername(),
                librarian.getFullName(),
                librarian.getEmail(),
                librarian.isEnabled()
        );
    }

    public AppUser getLibrarianById(Long id) {
        return findLibrarianEntityById(id);
    }

    @Transactional
    public void saveLibrarian(AppUserForm form) {
        if (form.getId() == null) {
            createLibrarian(form);
        } else {
            updateLibrarian(form);
        }
    }

    @Transactional
    public void toggleLibrarianStatus(Long id) {
        AppUser librarian = findLibrarianEntityById(id);
        librarian.setEnabled(!librarian.isEnabled());
        appUserRepository.save(librarian);
    }

    @Transactional
    public void resetLibrarianPassword(Long id, String newPassword) {
        if (!StringUtils.hasText(newPassword)) {
            throw new IllegalArgumentException("New password is required.");
        }

        if (newPassword.trim().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

        AppUser librarian = findLibrarianEntityById(id);
        librarian.setPassword(passwordEncoder.encode(newPassword.trim()));
        appUserRepository.save(librarian);
    }

    private void createLibrarian(AppUserForm form) {
        if (!StringUtils.hasText(form.getPassword())) {
            throw new IllegalArgumentException("Password is required for a new librarian.");
        }

        if (form.getPassword().trim().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

        String username = normalizeRequired(form.getUsername(), "Username is required.");

        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }

        AppUser librarian = new AppUser();
        librarian.setUsername(username);
        librarian.setFullName(normalizeRequired(form.getFullName(), "Full name is required."));
        librarian.setEmail(normalizeOptional(form.getEmail()));
        librarian.setPassword(passwordEncoder.encode(form.getPassword().trim()));
        librarian.setRole(Role.ROLE_LIBRARIAN);
        librarian.setEnabled(form.getEnabled() == null || form.getEnabled());

        appUserRepository.save(librarian);
    }

    private void updateLibrarian(AppUserForm form) {
        AppUser librarian = findLibrarianEntityById(form.getId());

        String username = normalizeRequired(form.getUsername(), "Username is required.");

        appUserRepository.findByUsername(username)
                .filter(existingUser -> !existingUser.getId().equals(form.getId()))
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException("Username already exists.");
                });

        librarian.setUsername(username);
        librarian.setFullName(normalizeRequired(form.getFullName(), "Full name is required."));
        librarian.setEmail(normalizeOptional(form.getEmail()));
        librarian.setEnabled(form.getEnabled() == null || form.getEnabled());

        if (StringUtils.hasText(form.getPassword())) {
            if (form.getPassword().trim().length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters.");
            }

            librarian.setPassword(passwordEncoder.encode(form.getPassword().trim()));
        }

        appUserRepository.save(librarian);
    }

    private AppUser findLibrarianEntityById(Long id) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Librarian not found."));

        if (user.getRole() != Role.ROLE_LIBRARIAN) {
            throw new IllegalArgumentException("Selected user is not a librarian.");
        }

        return user;
    }

    private boolean containsIgnoreCase(String value, String searchText) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(searchText);
    }

    private String normalizeRequired(String value, String errorMessage) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(errorMessage);
        }

        return value.trim();
    }

    private String normalizeOptional(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value.trim();
    }
}