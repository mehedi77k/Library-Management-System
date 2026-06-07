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
# Library Management System

Spring Boot web application for managing books, members, and borrow/return workflows.

## Status
- Java 21, Spring Boot 3.5.0
- Application configured to run on port `8080` by default
- Demo data (users, books, members) seeded at startup

## Features
- Dashboard overview for books, members, and borrowing stats
- Manage books, members, and borrow records (create / edit / search)
- Issue and return books with due dates and fine calculation
- Role-based access control (Admin, Librarian)

## Tech Stack
- Java 21
- Spring Boot 3.5
- Spring MVC, Spring Data JPA (Hibernate)
- Spring Security
- Thymeleaf (server-side templates)
- MySQL
- Maven

## Project Layout (key paths)
- `src/main/java/com/librarymanagement` — application code
- `src/main/java/com/librarymanagement/config/DataSeeder.java` — demo data seeder
- `src/main/resources/templates` — Thymeleaf templates
- `src/main/resources/static` — static assets (CSS)
- `src/main/resources/application.properties` — runtime configuration

## Prerequisites
- Java 21
- Maven 3.8+
- MySQL 8+

## Setup
1. Create a MySQL database (example):
   ```sql
   CREATE DATABASE library_management_system;
   ```
2. Update database credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/library_management_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```

## Build & Run
From the project root:

Development (run with Maven):
```bash
mvn spring-boot:run
```

Package and run the executable JAR:
```bash
mvn clean package
# then
java -jar target/library-management-system-1.0.0.jar
```

If you see a file named `library-management-system-1.0.0.jar.original` in `target/`, your build tool produced the shaded JAR; the final runnable JAR will typically be named `library-management-system-1.0.0.jar` after the packaging step.

The app starts on http://localhost:8080

## Tests
Run unit/integration tests with:
```bash
mvn test
```

## Default Accounts (created by `DataSeeder`)
| Role | Username | Password |
| --- | --- | --- |
| Admin | admin | admin123 |
| Librarian | librarian | lib123 |

## Key Endpoints / Pages
- `/login` — Login screen
- `/dashboard` — Summary dashboard
- `/books` — Book list, add/edit, search
- `/members` — Member list, add/edit, search
- `/borrow/issue` — Issue a book (default borrowing days: 14)
- `/borrow/records` — Borrow records and returns

## Borrowing Rules (implemented)
- Default borrowing period (UI/controller default): 14 days
- Fine calculation: 10.00 per day late (`LibraryService.DAILY_FINE_AMOUNT`)
- Books cannot be deleted while issued; members with active borrows cannot be deleted

## Configuration Notes
- Main settings are in `src/main/resources/application.properties`:
  - `server.port=8080`
  - `spring.jpa.hibernate.ddl-auto=update`
  - `spring.jpa.show-sql=true`

## Developer Notes
- Demo data is seeded in `src/main/java/com/librarymanagement/config/DataSeeder.java`.
- Controllers and services are under `src/main/java/com/librarymanagement/controller` and `src/main/java/com/librarymanagement/service` respectively.
- Templates are in `src/main/resources/templates` and static assets in `src/main/resources/static`.

## Dependencies
See [DEPENDENCIES.md](DEPENDENCIES.md) for a summarized dependency list.

## Next Steps
- Update `application.properties` with production-ready credentials and consider switching `spring.jpa.hibernate.ddl-auto` to `validate` or removing it for production.
- Commit the updated README and build artifacts.

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
