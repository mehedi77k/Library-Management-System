# Library Management System

Spring Boot library management system with MySQL, Spring Security, and a server-side Thymeleaf UI.

## Features
- Dashboard overview for books, members, and borrowing stats
- Manage books, members, and borrow records
- Issue and return books with due dates
- Role-based access (admin, librarian)
- Late return fine calculation
- Seeded demo data for quick start

## Tech Stack
- Java 21
- Spring Boot 3.5
- Spring MVC, Spring Data JPA (Hibernate)
- Spring Security
- Thymeleaf
- MySQL
- Maven

## Project Structure
- `src/main/java/com/librarymanagement` - Application code
- `src/main/resources/templates` - Thymeleaf templates
- `src/main/resources/static` - Static assets (CSS)
- `src/main/resources/application.properties` - App configuration

## Prerequisites
- Java 21
- Maven 3.8+
- MySQL 8+

## Setup
1. Create a MySQL database:
   ```sql
   CREATE DATABASE library_management_system;
   ```
2. Update database credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```

## Run Locally
From the project root:

```bash
mvn spring-boot:run
```

The app starts on http://localhost:8080

## Build
```bash
mvn clean package
```

## Test
```bash
mvn test
```

## Default Accounts
The data seeder creates two accounts on first run:

| Role | Username | Password |
| --- | --- | --- |
| Admin | admin | admin123 |
| Librarian | librarian | lib123 |

## Key Pages
- `/login` - Login screen
- `/dashboard` - Summary dashboard
- `/books` - Book list, add/edit, search
- `/members` - Member list, add/edit, search
- `/borrow/issue` - Issue a book
- `/borrow/records` - Borrow records and returns

## Access Control
- Admin: full access, including delete actions
- Librarian: dashboard, books, members, borrowing workflows

## Borrowing Rules
- Default borrowing period: 14 days
- Fine calculation: 10.00 per day late
- Books cannot be deleted while issued
- Members cannot be deleted with active borrowed books

## Configuration
Key settings in `src/main/resources/application.properties`:
- `server.port=8080`
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.jpa.show-sql=true`

## Notes
- The application uses `ddl-auto=update`, which auto-creates or updates tables based on entities.
- Sample books, members, and users are seeded at startup in `com.librarymanagement.config.DataSeeder`.
- Dependency list is summarized in [DEPENDENCIES.md](DEPENDENCIES.md).
