package com.librarymanagement.repository;

import com.librarymanagement.entity.AppUser;
import com.librarymanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    List<AppUser> findByRoleOrderByIdDesc(Role role);
}