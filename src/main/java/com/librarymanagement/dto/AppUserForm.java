package com.librarymanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AppUserForm {

    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 80, message = "Username must be between 3 and 80 characters")
    private String username;

    @NotBlank(message = "Full name is required")
    @Size(max = 120, message = "Full name cannot be more than 120 characters")
    private String fullName;

    @Email(message = "Enter a valid email address")
    @Size(max = 120, message = "Email cannot be more than 120 characters")
    private String email;

    private String password;

    private Boolean enabled = true;

    public AppUserForm() {
    }

    public AppUserForm(Long id, String username, String fullName, String email, Boolean enabled) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}