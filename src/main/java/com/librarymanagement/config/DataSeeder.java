package com.librarymanagement.config;

import com.librarymanagement.entity.AppUser;
import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.Member;
import com.librarymanagement.entity.Role;
import com.librarymanagement.repository.AppUserRepository;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            BookRepository bookRepository,
            MemberRepository memberRepository,
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedBooks();
        seedMembers();
    }

    private void seedUsers() {
        if (!appUserRepository.existsByUsername("admin")) {
            AppUser admin = new AppUser(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "System Administrator",
                    "admin@library.com",
                    Role.ROLE_ADMIN
            );

            appUserRepository.save(admin);
        }

        if (!appUserRepository.existsByUsername("librarian")) {
            AppUser librarian = new AppUser(
                    "librarian",
                    passwordEncoder.encode("lib123"),
                    "Default Librarian",
                    "librarian@library.com",
                    Role.ROLE_LIBRARIAN
            );

            appUserRepository.save(librarian);
        }
    }

    private void seedBooks() {
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book(
                    "Clean Code",
                    "Robert C. Martin",
                    "9780132350884",
                    "Programming",
                    5
            ));

            bookRepository.save(new Book(
                    "Effective Java",
                    "Joshua Bloch",
                    "9780134685991",
                    "Programming",
                    4
            ));

            bookRepository.save(new Book(
                    "Introduction to Algorithms",
                    "Thomas H. Cormen",
                    "9780262033848",
                    "Computer Science",
                    3
            ));
        }
    }

    private void seedMembers() {
        if (memberRepository.count() == 0) {
            memberRepository.save(new Member(
                    "Mehedi Hasan",
                    "mehedi@example.com",
                    "01700000000",
                    "Dhaka, Bangladesh"
            ));

            memberRepository.save(new Member(
                    "Rahim Uddin",
                    "rahim@example.com",
                    "01800000000",
                    "Daffodil Area, Dhaka"
            ));
        }
    }
}