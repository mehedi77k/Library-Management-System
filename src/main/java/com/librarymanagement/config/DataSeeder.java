package com.librarymanagement.config;

import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.Member;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public DataSeeder(
            BookRepository bookRepository,
            MemberRepository memberRepository
    ) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {
        seedBooks();
        seedMembers();
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