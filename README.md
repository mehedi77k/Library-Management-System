# Library Management System

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge\&logo=openjdk\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-6DB33F?style=for-the-badge\&logo=springboot\&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-Enabled-6DB33F?style=for-the-badge\&logo=springsecurity\&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge\&logo=mysql\&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Templates-005F0F?style=for-the-badge\&logo=thymeleaf\&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build%20Tool-C71A36?style=for-the-badge\&logo=apachemaven\&logoColor=white)
![Apache POI](https://img.shields.io/badge/Apache%20POI-Excel%20Export-217346?style=for-the-badge)
![OpenPDF](https://img.shields.io/badge/OpenPDF-PDF%20Export-B31B1B?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Functional%20Web%20Application-success?style=for-the-badge)

A full Java-based **Library Management System** built with **Spring Boot**, **Spring MVC**, **Spring Security**, **Thymeleaf**, **Spring Data JPA**, **MySQL**, **Apache POI**, and **OpenPDF**.

The system supports book management, member management, book issue/return workflow, role-based login, librarian management, advanced search and pagination, reports dashboard, PDF export, and Excel export.

---

## Table of Contents

* [Overview](#overview)
* [Core Features](#core-features)
* [Technology Stack](#technology-stack)
* [System Architecture](#system-architecture)
* [Project Structure](#project-structure)
* [Prerequisites](#prerequisites)
* [Database Setup](#database-setup)
* [Configuration](#configuration)
* [Run the Project](#run-the-project)
* [Default Login Accounts](#default-login-accounts)
* [Application Pages](#application-pages)
* [Access Control](#access-control)
* [Database Models](#database-models)
* [Borrowing Workflow](#borrowing-workflow)
* [Reports and Export System](#reports-and-export-system)
* [Development Commands](#development-commands)
* [Troubleshooting](#troubleshooting)
* [Security Notes](#security-notes)
* [Known Limitations](#known-limitations)
* [Future Improvements](#future-improvements)
* [Project Status](#project-status)

---

## Overview

**Library Management System** is a server-side rendered Java web application designed to automate common library operations.

It allows administrators and librarians to:

* Log in securely
* View dashboard statistics
* Manage books
* Manage members
* Issue books to members
* Return books
* Track issued and returned records
* Calculate late return fines
* Search and filter records
* View paginated data
* Manage librarian accounts
* Generate reports
* Export reports as PDF and Excel files

The project started as a Java console-based system and has been converted into a full Spring Boot web application with persistent MySQL storage and a professional layered architecture.

---

## Core Features

### Authentication and Authorization

* Custom login page
* Spring Security-based authentication
* BCrypt password encryption
* Role-based access control
* Admin and Librarian roles
* Secure logout
* Custom access-denied page
* CSRF-protected form submissions

Supported roles:

```text
ROLE_ADMIN
ROLE_LIBRARIAN
```

---

### Admin Features

Admin users can:

* Access the dashboard
* Add, edit, search, filter, and delete books
* Add, edit, search, filter, and delete members
* Issue books
* Return books
* View all borrow records
* Access reports
* Export PDF and Excel reports
* Add librarians
* Edit librarians
* Enable or disable librarian accounts
* Reset librarian passwords

---

### Librarian Features

Librarian users can:

* Access the dashboard
* View and manage books
* View and manage members
* Issue books
* Return books
* View borrow records
* Use search, filtering, and pagination
* View reports
* Export reports

Librarians cannot:

* Delete books
* Delete members
* Manage librarian accounts

---

### Book Management

The system supports:

* Add new books
* Edit existing books
* Search books by title, author, ISBN, or category
* Filter books by category
* Filter books by availability
* Paginated book listing
* Track total copies
* Track available copies
* Prevent deleting books with active issued records

Book fields include:

```text
ID
Title
Author
ISBN
Category
Total Copies
Available Copies
Created Date
```

---

### Member Management

The system supports:

* Add new members
* Edit existing members
* Search members by name, email, phone, or address
* Filter members by active/inactive status
* Paginated member listing
* Prevent deleting members with active borrowed books

Member fields include:

```text
ID
Name
Email
Phone
Address
Active Status
Created Date
```

---

### Borrowing and Returning

The system supports:

* Issue book to member
* Select borrowing duration
* Default borrowing period of 14 days
* Return issued book
* Auto-update available book copies
* Auto-calculate late return fine
* Track issued records
* Track returned records
* Prevent unavailable books from being issued
* Prevent inactive members from borrowing books

Borrowing rules:

```text
Default borrowing period: 14 days
Fine rate: BDT 10.00 per late day
Only available books can be issued
Inactive members cannot borrow books
Returned books increase available copy count
```

---

### Advanced Search and Pagination

The system includes advanced filtering for:

#### Books

* Search by title
* Search by author
* Search by ISBN
* Search by category
* Filter by category
* Filter by availability
* Page size selector

#### Members

* Search by name
* Search by email
* Search by phone
* Search by address
* Filter by active/inactive status
* Page size selector

#### Borrow Records

* Search by book title
* Search by ISBN
* Search by member name
* Search by member email
* Search by member phone
* Filter by issued/returned status
* Filter overdue records
* Page size selector

Supported page sizes:

```text
5
10
20
50
```

---

### Librarian Management

Admin users can manage librarian accounts.

Features:

* Add librarian
* Edit librarian
* Enable librarian account
* Disable librarian account
* Reset librarian password
* Search librarian by username, full name, or email
* Encrypted password storage using BCrypt

Librarian account fields:

```text
ID
Username
Password
Full Name
Email
Role
Enabled Status
Created Date
```

---

### Reports Module

The system includes a complete reports module.

Available reports:

* Reports Dashboard
* Overdue Books Report
* Issued Books Report
* Returned Books Report
* Fine Collection Report
* Member Borrowing History Report
* Most Borrowed Books Report

Reports can be viewed from:

```text
/reports
```

---

### PDF and Excel Export

The system supports downloadable reports.

PDF export:

* Reports Summary PDF
* Overdue Books Report PDF
* Fine Collection Report PDF

Excel export:

* Overdue Books Report Excel
* Issued Books Report Excel
* Returned Books Report Excel
* Fine Collection Report Excel
* Member Borrowing History Excel
* Most Borrowed Books Excel

Export libraries:

```text
Apache POI for Excel files
OpenPDF for PDF files
```

---

## Technology Stack

| Layer             | Technologies               |
| ----------------- | -------------------------- |
| Language          | Java 21                    |
| Backend Framework | Spring Boot 3.5.0          |
| Web Layer         | Spring MVC                 |
| Template Engine   | Thymeleaf                  |
| Security          | Spring Security, BCrypt    |
| Database          | MySQL                      |
| ORM               | Spring Data JPA, Hibernate |
| Validation        | Jakarta Bean Validation    |
| Build Tool        | Maven                      |
| PDF Export        | OpenPDF                    |
| Excel Export      | Apache POI                 |
| Frontend Styling  | HTML, CSS                  |
| Server            | Embedded Tomcat            |

---

## System Architecture

```text
┌───────────────────────────────────────────────────────────────┐
│                         User Layer                            │
│                                                               │
│        ┌────────────────────┐    ┌────────────────────┐       │
│        │       Admin        │    │     Librarian      │       │
│        │                    │    │                    │       │
│        │ - Full access      │    │ - Book workflow    │       │
│        │ - Manage users     │    │ - Member workflow  │       │
│        │ - Delete records   │    │ - Issue/return     │       │
│        │ - Export reports   │    │ - View reports     │       │
│        └─────────┬──────────┘    └─────────┬──────────┘       │
└──────────────────┼─────────────────────────┼──────────────────┘
                   │                         │
                   ▼                         ▼
┌───────────────────────────────────────────────────────────────┐
│                      Presentation Layer                       │
│                                                               │
│                    Thymeleaf HTML Templates                   │
│                                                               │
│ Pages: Login, Dashboard, Books, Members, Borrow Records,      │
│ Issue Book, Librarians, Reports, Access Denied                │
└───────────────────────────────┬───────────────────────────────┘
                                │
                                ▼
┌───────────────────────────────────────────────────────────────┐
│                        Controller Layer                       │
│                                                               │
│ AuthController, DashboardController, BookController,          │
│ MemberController, BorrowController, LibrarianController,      │
│ ReportController, ReportExportController                      │
└───────────────────────────────┬───────────────────────────────┘
                                │
                                ▼
┌───────────────────────────────────────────────────────────────┐
│                         Service Layer                         │
│                                                               │
│ LibraryService, AppUserService, ReportService, ExportService  │
│                                                               │
│ Business Logic: Issue, Return, Fine Calculation,              │
│ User Management, Reports, PDF/Excel Export                    │
└───────────────────────────────┬───────────────────────────────┘
                                │
                                ▼
┌───────────────────────────────────────────────────────────────┐
│                       Repository Layer                        │
│                                                               │
│ AppUserRepository, BookRepository, MemberRepository,          │
│ BorrowRecordRepository                                        │
└───────────────────────────────┬───────────────────────────────┘
                                │
                                ▼
┌───────────────────────────────────────────────────────────────┐
│                         Database Layer                        │
│                                                               │
│                              MySQL                            │
│                                                               │
│ Tables: app_users, books, members, borrow_records             │
└───────────────────────────────────────────────────────────────┘
```

---

## Project Structure

```text
Library-Management-System
│
├── pom.xml
├── README.md
├── DEPENDENCIES.md
├── .gitignore
│
└── src
    └── main
        ├── java
        │   └── com
        │       └── librarymanagement
        │           ├── LibraryManagementApplication.java
        │           │
        │           ├── config
        │           │   ├── DataSeeder.java
        │           │   └── SecurityConfig.java
        │           │
        │           ├── controller
        │           │   ├── AccessDeniedController.java
        │           │   ├── AuthController.java
        │           │   ├── BookController.java
        │           │   ├── BorrowController.java
        │           │   ├── DashboardController.java
        │           │   ├── LibrarianController.java
        │           │   ├── MemberController.java
        │           │   ├── ReportController.java
        │           │   └── ReportExportController.java
        │           │
        │           ├── dto
        │           │   ├── AppUserForm.java
        │           │   ├── MostBorrowedBookReportRow.java
        │           │   ├── OverdueReportRow.java
        │           │   └── ReportSummary.java
        │           │
        │           ├── entity
        │           │   ├── AppUser.java
        │           │   ├── Book.java
        │           │   ├── BorrowRecord.java
        │           │   ├── BorrowStatus.java
        │           │   ├── Member.java
        │           │   └── Role.java
        │           │
        │           ├── repository
        │           │   ├── AppUserRepository.java
        │           │   ├── BookRepository.java
        │           │   ├── BorrowRecordRepository.java
        │           │   └── MemberRepository.java
        │           │
        │           └── service
        │               ├── AppUserService.java
        │               ├── ExportService.java
        │               ├── LibraryService.java
        │               └── ReportService.java
        │
        └── resources
            ├── application.properties
            │
            ├── static
            │   └── css
            │       └── style.css
            │
            └── templates
                ├── access-denied.html
                ├── dashboard.html
                ├── login.html
                │
                ├── books
                │   ├── form.html
                │   └── list.html
                │
                ├── borrow
                │   ├── issue.html
                │   └── records.html
                │
                ├── fragments
                │   ├── footer.html
                │   ├── head.html
                │   ├── messages.html
                │   └── navbar.html
                │
                ├── librarians
                │   ├── form.html
                │   ├── list.html
                │   └── reset-password.html
                │
                ├── members
                │   ├── form.html
                │   └── list.html
                │
                └── reports
                    ├── dashboard.html
                    ├── fines.html
                    ├── issued.html
                    ├── member-history.html
                    ├── most-borrowed.html
                    ├── overdue.html
                    └── returned.html
```

---

## Prerequisites

Install the following before running the project:

* Java JDK 21
* Apache Maven 3.8+
* MySQL Server or XAMPP MySQL
* Git
* IntelliJ IDEA, Eclipse, Spring Tool Suite, or VS Code

Recommended local setup:

```text
Java: 21
Maven: 3.8+
Database: MySQL 8+ or XAMPP MySQL/MariaDB
Port: 8080
```

---

## Database Setup

Create a MySQL database before running the project.

### Using MySQL Terminal

```sql
CREATE DATABASE library_management_system;
```

### Using phpMyAdmin / XAMPP

1. Start XAMPP.
2. Start MySQL.
3. Open:

```text
http://localhost/phpmyadmin
```

4. Click **New**.
5. Create a database named:

```text
library_management_system
```

No manual table creation is required. Hibernate will create/update tables automatically because the project uses:

```properties
spring.jpa.hibernate.ddl-auto=update
```

---

## Configuration

The main configuration file is:

```text
src/main/resources/application.properties
```

Current local development configuration:

```properties
spring.application.name=Library Management System

server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/library_management_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.thymeleaf.cache=false
```

### XAMPP MySQL Note

If you are using XAMPP and phpMyAdmin opens without a password, keep:

```properties
spring.datasource.username=root
spring.datasource.password=
```

If your MySQL has a password, update it:

```properties
spring.datasource.password=your_mysql_password
```

If your MySQL runs on a different port, such as `3307`, update the URL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/library_management_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
```

---

## Run the Project

### 1. Clone the Repository

```bash
git clone https://github.com/mehedi77k/Library-Management-System.git
cd Library-Management-System
```

### 2. Create the Database

```sql
CREATE DATABASE library_management_system;
```

### 3. Update Database Credentials

Edit:

```text
src/main/resources/application.properties
```

Set your MySQL username and password.

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

### 6. Open the Application

```text
http://localhost:8080
```

The application will redirect to:

```text
http://localhost:8080/login
```

---

## Run as Executable JAR

Build the JAR:

```bash
mvn clean package
```

Run the JAR:

```bash
java -jar target/library-management-system-1.0.0.jar
```

Open:

```text
http://localhost:8080
```

---

## Default Login Accounts

The system seeds two default users on first run through `DataSeeder.java`.

| Role      | Username    | Password   |
| --------- | ----------- | ---------- |
| Admin     | `admin`     | `admin123` |
| Librarian | `librarian` | `lib123`   |

Default seed data also includes sample books and members.

Sample books:

```text
Clean Code
Effective Java
Introduction to Algorithms
```

Sample members:

```text
Mehedi Hasan
Rahim Uddin
```

---

## Application Pages

| Page                     | URL                               | Access                                     |
| ------------------------ | --------------------------------- | ------------------------------------------ |
| Login                    | `/login`                          | Public                                     |
| Dashboard                | `/dashboard`                      | Admin, Librarian                           |
| Books                    | `/books`                          | Admin, Librarian                           |
| Add Book                 | `/books/add`                      | Admin, Librarian                           |
| Edit Book                | `/books/edit/{id}`                | Admin, Librarian                           |
| Members                  | `/members`                        | Admin, Librarian                           |
| Add Member               | `/members/add`                    | Admin, Librarian                           |
| Edit Member              | `/members/edit/{id}`              | Admin, Librarian                           |
| Issue Book               | `/borrow/issue`                   | Admin, Librarian                           |
| Borrow Records           | `/borrow/records`                 | Admin, Librarian                           |
| Librarians               | `/librarians`                     | Admin only                                 |
| Add Librarian            | `/librarians/add`                 | Admin only                                 |
| Edit Librarian           | `/librarians/edit/{id}`           | Admin only                                 |
| Reset Librarian Password | `/librarians/reset-password/{id}` | Admin only                                 |
| Reports Dashboard        | `/reports`                        | Admin, Librarian                           |
| Overdue Report           | `/reports/overdue`                | Admin, Librarian                           |
| Issued Report            | `/reports/issued`                 | Admin, Librarian                           |
| Returned Report          | `/reports/returned`               | Admin, Librarian                           |
| Fine Report              | `/reports/fines`                  | Admin, Librarian                           |
| Member History Report    | `/reports/member-history`         | Admin, Librarian                           |
| Most Borrowed Books      | `/reports/most-borrowed`          | Admin, Librarian                           |
| Access Denied            | `/access-denied`                  | Public route, shown after forbidden access |

---

## Access Control

### Admin Access

Admin users can:

```text
Access dashboard
Manage books
Delete books
Manage members
Delete members
Issue books
Return books
View borrow records
Manage librarians
Reset librarian passwords
View reports
Export PDF/Excel reports
```

### Librarian Access

Librarian users can:

```text
Access dashboard
Manage books
Manage members
Issue books
Return books
View borrow records
View reports
Export PDF/Excel reports
```

Librarian users cannot:

```text
Delete books
Delete members
Manage librarian accounts
Reset librarian passwords
```

---

## Database Models

### `app_users`

Stores admin and librarian login accounts.

| Field        | Description                      |
| ------------ | -------------------------------- |
| `id`         | Primary key                      |
| `username`   | Unique login username            |
| `password`   | BCrypt encrypted password        |
| `full_name`  | User full name                   |
| `email`      | Optional email                   |
| `role`       | `ROLE_ADMIN` or `ROLE_LIBRARIAN` |
| `enabled`    | Account enabled/disabled status  |
| `created_at` | Account creation timestamp       |

---

### `books`

Stores library book information.

| Field              | Description                          |
| ------------------ | ------------------------------------ |
| `id`               | Primary key                          |
| `title`            | Book title                           |
| `author`           | Book author                          |
| `isbn`             | Unique ISBN                          |
| `category`         | Book category                        |
| `total_copies`     | Total number of copies               |
| `available_copies` | Number of currently available copies |
| `created_at`       | Book creation timestamp              |

---

### `members`

Stores library member information.

| Field        | Description               |
| ------------ | ------------------------- |
| `id`         | Primary key               |
| `name`       | Member name               |
| `email`      | Unique member email       |
| `phone`      | Member phone number       |
| `address`    | Member address            |
| `active`     | Active/inactive status    |
| `created_at` | Member creation timestamp |

---

### `borrow_records`

Stores issued and returned book records.

| Field         | Description             |
| ------------- | ----------------------- |
| `id`          | Primary key             |
| `book_id`     | Linked book ID          |
| `member_id`   | Linked member ID        |
| `issue_date`  | Book issue date         |
| `due_date`    | Due date                |
| `return_date` | Return date             |
| `status`      | `ISSUED` or `RETURNED`  |
| `fine_amount` | Late return fine amount |

---

## Borrowing Workflow

```text
Admin/Librarian logs in
        ↓
Opens Issue Book page
        ↓
Selects available book
        ↓
Selects active member
        ↓
Sets borrowing days
        ↓
System creates borrow record
        ↓
Available book copy decreases
        ↓
Book appears in borrow records as ISSUED
        ↓
Admin/Librarian returns the book
        ↓
System sets return date
        ↓
System calculates fine if late
        ↓
Available book copy increases
        ↓
Record status becomes RETURNED
```

---

## Fine Calculation

Fine calculation is handled inside `LibraryService`.

Current rule:

```text
Fine = Late Days × BDT 10.00
```

Example:

```text
Due date: 2026-06-01
Return date: 2026-06-04
Late days: 3
Fine: 3 × 10.00 = BDT 30.00
```

If the book is returned on or before the due date:

```text
Fine = BDT 0.00
```

---

## Reports and Export System

### Reports Dashboard

URL:

```text
/reports
```

Shows:

```text
Total books
Available books
Total members
Issued books
Returned books
Overdue books
Top borrowed books
Current overdue books
```

---

### Overdue Books Report

URL:

```text
/reports/overdue
```

Shows:

```text
Record ID
Book title
Member name
Issue date
Due date
Overdue days
Estimated fine
```

Exports:

```text
PDF
Excel
```

Export URLs:

```text
/reports/export/overdue/pdf
/reports/export/overdue/excel
```

---

### Issued Books Report

URL:

```text
/reports/issued
```

Shows all currently issued books.

Exports:

```text
Excel
```

Export URL:

```text
/reports/export/issued/excel
```

---

### Returned Books Report

URL:

```text
/reports/returned
```

Shows all returned books.

Exports:

```text
Excel
```

Export URL:

```text
/reports/export/returned/excel
```

---

### Fine Collection Report

URL:

```text
/reports/fines
```

Shows:

```text
Total fine collected
Fine records
Book
Member
Due date
Return date
Fine amount
```

Exports:

```text
PDF
Excel
```

Export URLs:

```text
/reports/export/fines/pdf
/reports/export/fines/excel
```

---

### Member Borrowing History

URL:

```text
/reports/member-history
```

Shows complete borrowing history for a selected member.

Exports:

```text
Excel
```

Export URL:

```text
/reports/export/member-history/excel?memberId={memberId}
```

---

### Most Borrowed Books Report

URL:

```text
/reports/most-borrowed
```

Shows books ranked by total borrow count.

Exports:

```text
Excel
```

Export URL:

```text
/reports/export/most-borrowed/excel
```

---

## Exported Files

Expected exported filenames:

```text
reports-summary.pdf
overdue-books-report.pdf
overdue-books-report.xlsx
issued-books-report.xlsx
returned-books-report.xlsx
fine-collection-report.pdf
fine-collection-report.xlsx
member-borrowing-history.xlsx
most-borrowed-books.xlsx
```

---

## Development Commands

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

### Package

```bash
mvn clean package
```

### Run JAR

```bash
java -jar target/library-management-system-1.0.0.jar
```

### Run Tests

```bash
mvn test
```

### Clean Build Files

```bash
mvn clean
```

---

## Troubleshooting

### `mvn` is not recognized

Maven is not installed or not added to PATH.

Check:

```bash
mvn -v
```

If it fails:

1. Install Apache Maven.
2. Add Maven `bin` folder to system PATH.
3. Restart terminal or VS Code.

---

### Java version error

Check Java version:

```bash
java -version
```

Expected:

```text
Java 21
```

If Java is missing, install JDK 21 and set `JAVA_HOME`.

---

### MySQL connection error

Common error:

```text
Communications link failure
```

Check:

1. MySQL is running.
2. Database exists.
3. Port is correct.
4. Username/password are correct.

For XAMPP:

```text
Start XAMPP → Start MySQL → Open phpMyAdmin
```

---

### Database tables are not created

Check that your database exists:

```sql
CREATE DATABASE library_management_system;
```

Then make sure this setting exists:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Restart the application.

---

### Login does not work

Default accounts are created only if they do not already exist.

If the `app_users` table has wrong or old data, you can reset it during development:

```sql
DROP TABLE app_users;
```

Then restart the application.

The seeder will recreate:

```text
admin / admin123
librarian / lib123
```

---

### Port 8080 already in use

Change the port in `application.properties`:

```properties
server.port=8081
```

Then open:

```text
http://localhost:8081
```

---

### PDF or Excel export fails

Run:

```bash
mvn clean install
```

Make sure these dependencies exist in `pom.xml`:

```text
Apache POI
OpenPDF
```

If Maven dependency download fails, check your internet connection and run:

```bash
mvn dependency:purge-local-repository
mvn clean install
```

---

### Access denied page appears

This is expected when a user tries to access a route without permission.

Example:

```text
Librarian tries to open /librarians
```

Expected result:

```text
Access Denied
```

---

## Security Notes

Before production use:

* Change default admin password.
* Remove or disable default demo users.
* Do not expose database credentials publicly.
* Do not commit real passwords or secrets.
* Use environment variables for database credentials.
* Disable `spring.jpa.show-sql` in production.
* Avoid `spring.jpa.hibernate.ddl-auto=update` in production.
* Use proper database migrations such as Flyway or Liquibase.
* Use HTTPS in production.
* Add stronger password policy.
* Add login rate limiting.
* Add audit logs for issue, return, delete, and password reset actions.
* Restrict librarian account creation to trusted admins only.
* Back up MySQL database regularly.

---

## Known Limitations

* The project currently uses Hibernate `ddl-auto=update` instead of a formal migration tool.
* Demo users and sample data are seeded automatically in development.
* There is no email notification system.
* There is no student/member self-service login.
* There is no barcode/QR-based book scanning.
* There is no book cover upload feature.
* There is no audit log for sensitive actions.
* There is no REST API layer for external clients.
* Reports are generated from current database records only.
* PDF export is available for selected reports, not every page.
* Excel export is focused on reports, not all CRUD tables.
* Fine amount is fixed at BDT 10.00 per day in code.
* The project is designed for local/manual deployment, not cloud deployment yet.

---

## Future Improvements

* Add Flyway or Liquibase database migrations
* Add full REST API endpoints
* Add user profile page
* Add change password feature
* Add admin password reset for all users
* Add member/student login portal
* Add book reservation system
* Add book category management
* Add publisher and publication year fields
* Add shelf location field
* Add book cover image upload
* Add barcode or QR code for books
* Add scan-based issue/return workflow
* Add email notifications
* Add SMS notifications
* Add audit log module
* Add dashboard charts
* Add print-friendly reports
* Add more PDF exports
* Add Excel export for books and members
* Add CSV export
* Add overdue email reminders
* Add automatic fine update job
* Add unit tests for services
* Add integration tests for controllers
* Add Docker support
* Add deployment guide
* Add production profile
* Add environment-variable based configuration
* Add CI/CD with GitHub Actions

---

## Repository

```text
https://github.com/mehedi77k/Library-Management-System
```

---

## Project Status

```text
Status: Functional Web Application
Project Type: Library Management System
Backend: Java Spring Boot
Frontend: Thymeleaf Server-Side Templates
Database: MySQL
Authentication: Spring Security
Authorization: Role-Based Access Control
Roles: Admin, Librarian
PDF Export: OpenPDF
Excel Export: Apache POI
Build Tool: Maven
Java Version: 21
```

---

## Author

```text
Mehedi Hasan
```

---

## License

This project is licensed under the MIT License.

See the [LICENSE](LICENSE) file for details.
