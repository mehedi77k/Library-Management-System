# Library Management System

Spring Boot library management system with MySQL and Thymeleaf UI.

## Features
- Manage books, members, and borrow records
- Issue and track borrowing status
- Dashboard overview
- Server-side rendered UI with Thymeleaf

## Tech Stack
- Java 17
- Spring Boot 3.5
- Spring MVC, Spring Data JPA (Hibernate)
- Thymeleaf
- MySQL
- Maven

## Project Structure
- `src/main/java/com/librarymanagement` - Application code
- `src/main/resources/templates` - Thymeleaf templates
- `src/main/resources/static` - Static assets (CSS)
- `src/main/resources/application.properties` - App configuration

## Prerequisites
- Java 17
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

## Configuration
Key settings in `src/main/resources/application.properties`:
- `server.port=8080`
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.jpa.show-sql=true`

## Notes
- The application uses `ddl-auto=update`, which auto-creates/updates tables based on entities.
- A data seeder is available in `com.librarymanagement.config.DataSeeder`.
