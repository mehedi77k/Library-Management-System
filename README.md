# Library Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Object--Oriented%20Programming-blue?style=for-the-badge)
![Console App](https://img.shields.io/badge/App-Console%20Based-6C63FF?style=for-the-badge)
![Database](https://img.shields.io/badge/Database-Not%20Used-lightgrey?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

A simple and lightweight **Library Management System** built using **Java**.  
This project demonstrates core **Object-Oriented Programming**, basic library operations, and runtime data handling without using any external database.

---

## Overview

**Library Management System** is a completed Java console application designed to manage basic library operations such as adding books, viewing available books, issuing books, and returning books.

The project is beginner-friendly and focuses on applying core Java concepts in a practical system. It does not use MySQL, PostgreSQL, SQLite, or any external database. Instead, all data is stored temporarily in memory using Java objects and collections.

This makes the project useful for learning:

- Java programming fundamentals
- Object-Oriented Programming
- Classes and objects
- Runtime data handling
- Basic CRUD operations
- Console-based menu systems
- Simple system design logic

---

## Key Features

- Add new books to the library
- View all available books
- Issue books to users
- Return issued books
- Track book availability
- Store book information during runtime
- Console-based user interaction
- Object-oriented project structure
- No external database required
- Lightweight and easy to run
- Beginner-friendly Java implementation

---

## Project Type

```text
Project Name: Library Management System
Project Type: Console Application
Programming Language: Java
Database: Not Used
Storage Type: In-memory runtime storage
Status: Completed
```

---

## Technology Stack

| Category | Technology |
|---|---|
| Language | Java |
| Application Type | Console-based application |
| Programming Style | Object-Oriented Programming |
| Data Storage | In-memory data structures |
| Database | Not used |
| IDE Support | IntelliJ IDEA, Eclipse, NetBeans, VS Code |

---

## Concepts Used

This project demonstrates the following Java concepts:

- Classes and objects
- Encapsulation
- Methods
- Constructors
- Conditional statements
- Loops
- Arrays or ArrayList
- Menu-driven programming
- Basic input handling
- Runtime data management
- Object-Oriented Programming principles

---

## Main Features

### Book Management

The system allows library books to be managed from the console.

Features include:

- Add new books
- View all books
- Store book details
- Track book availability
- Display current library records

---

### Issue Book

The issue book feature allows a book to be assigned to a user if it is available.

Features include:

- Check whether the book exists
- Check whether the book is available
- Mark the book as issued
- Prevent unavailable books from being issued again
- Update book status after issuing

---

### Return Book

The return book feature updates the status of an issued book.

Features include:

- Return previously issued books
- Update book availability
- Make returned books available again
- Maintain simple issue-return logic

---

### Runtime Record Handling

The system does not use permanent storage.

Instead, it handles records using:

- Java objects
- Arrays or ArrayList
- Runtime memory
- Object references

When the program closes, the stored data is cleared.

---

## System Workflow

```text
Start Program
     │
     ▼
Display Menu
     │
     ├── Add Book
     ├── View Books
     ├── Issue Book
     ├── Return Book
     └── Exit
     │
     ▼
Perform Selected Operation
     │
     ▼
Update Runtime Data
     │
     ▼
Return to Menu or Exit
```

---

## How the System Works

1. The program starts and displays a console menu.
2. The user selects an operation from the menu.
3. The system performs the selected operation.
4. Book records are stored using Java objects and in-memory structures.
5. When a book is issued, the system checks its availability.
6. When a book is returned, the system updates its status.
7. The menu continues until the user exits the program.
8. Since no database is used, all data resets after the program is closed.

---

## Data Handling

This project does not use a database.

Data is handled using Java runtime memory.

```text
Book records are stored temporarily while the program is running.
After the program exits, all records are cleared.
```

### Storage Method

| Data Type | Storage Method |
|---|---|
| Book details | Java object |
| Book list | Array or ArrayList |
| Availability status | Object property |
| Issued/returned state | Runtime variable |

---

## Expected Project Structure

```text
Library-Management-System/
│
├── src/
│   ├── Main.java
│   ├── Book.java
│   ├── Library.java
│   └── ...
│
└── README.md
```

Depending on your IDE or setup, the Java files may also be placed directly in the project root.

---

## File Description

| File | Description |
|---|---|
| `Main.java` | Starts the program and handles the main menu |
| `Book.java` | Represents book details such as title, ID, author, and availability |
| `Library.java` | Handles book operations such as add, view, issue, and return |
| `README.md` | Project documentation |

---

## Requirements

Before running the project, make sure you have:

- Java JDK 8 or above
- A Java IDE or text editor
- Basic knowledge of Java
- Terminal, Command Prompt, or IDE console

Recommended IDEs:

- IntelliJ IDEA
- Eclipse
- NetBeans
- Visual Studio Code

---

## Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/mehedi77k/Library-Management-System.git
```

### 2. Open the Project Folder

```bash
cd Library-Management-System
```

### 3. Open in an IDE

Open the project using any Java-supported IDE:

- IntelliJ IDEA
- Eclipse
- NetBeans
- VS Code

---

## Compile and Run

### Option 1: If Java Files Are Inside `src`

Compile:

```bash
javac src/*.java
```

Run:

```bash
java -cp src Main
```

### Option 2: If Java Files Are in the Root Folder

Compile:

```bash
javac Main.java
```

Run:

```bash
java Main
```

### Option 3: Run from IDE

1. Open the project in your IDE.
2. Locate `Main.java`.
3. Run the `main()` method.
4. Use the console menu to operate the system.

---

## Sample Menu Flow

```text
===== Library Management System =====

1. Add Book
2. View All Books
3. Issue Book
4. Return Book
5. Exit

Enter your choice:
```

Example flow:

```text
Enter your choice: 1
Enter book name: Java Programming
Enter author name: James Gosling
Book added successfully.
```

```text
Enter your choice: 3
Enter book ID: 101
Book issued successfully.
```

```text
Enter your choice: 4
Enter book ID: 101
Book returned successfully.
```

---

## Functional Requirements

The system supports the following operations:

| Requirement | Status |
|---|---|
| Add book | Supported |
| View books | Supported |
| Issue book | Supported |
| Return book | Supported |
| Track availability | Supported |
| Store data during runtime | Supported |
| Database storage | Not included |
| GUI | Not included |

---

## Learning Objectives

This project is useful for understanding how a simple software system is designed using Java.

By studying this project, learners can understand:

- How to structure a Java console application
- How to define classes and objects
- How to store and manage objects in memory
- How to implement basic CRUD operations
- How to use conditional logic in real-world operations
- How to build a menu-driven program
- How to separate responsibilities between classes

---

## Testing Checklist

Use this checklist to verify the completed project:

- [ ] Program starts successfully
- [ ] Main menu displays correctly
- [ ] User input is accepted properly
- [ ] New books can be added
- [ ] All books can be viewed
- [ ] Book availability is shown correctly
- [ ] Available books can be issued
- [ ] Already issued books cannot be issued again
- [ ] Issued books can be returned
- [ ] Returned books become available again
- [ ] Invalid input is handled properly
- [ ] Program exits without errors

---

## Example Use Cases

### Use Case 1: Add a New Book

A librarian enters book details, and the system stores the book in memory.

```text
Input: Book ID, title, author
Output: Book added successfully
```

### Use Case 2: Issue a Book

A user requests a book. The system checks availability and updates the book status.

```text
Input: Book ID
Output: Book issued successfully
```

### Use Case 3: Return a Book

A user returns an issued book. The system updates the book status to available.

```text
Input: Book ID
Output: Book returned successfully
```

### Use Case 4: View All Books

The system displays all stored books and their current availability status.

```text
Output: List of books with details
```

---

## Known Limitations

- Data is not saved permanently.
- No external database is used.
- Records are cleared after the program exits.
- The project is console-based only.
- No graphical user interface is included.
- No login or authentication system is included.
- No multi-user access control is included.
- No fine calculation system is included.
- No advanced search or filtering is included.

---

## Future Improvements

Although the core project is complete, the following features can be added in future versions:

- Add MySQL or PostgreSQL database support
- Add file-based storage using text, CSV, or JSON files
- Create a GUI using Java Swing or JavaFX
- Add admin and user login system
- Add student or member management
- Add book search by title, author, or ID
- Add fine calculation for late returns
- Add issue and return date tracking
- Add report generation
- Add input validation improvements
- Add exception handling improvements
- Add unit tests
- Add persistent data storage

---

## Why This Project Is Useful

This project is useful for beginners because it keeps the system simple while demonstrating real programming logic.

It focuses on:

- Writing clean Java code
- Understanding object relationships
- Applying OOP concepts
- Building a working console application
- Practicing basic software design
- Managing data without external tools

---

## Contributing

Contributions are welcome.

To contribute:

1. Fork the repository

2. Create a new branch

```bash
git checkout -b feature/new-feature
```

3. Commit your changes

```bash
git commit -m "Add new feature"
```

4. Push the branch

```bash
git push origin feature/new-feature
```

5. Open a Pull Request

---

## Developer

**Mehedi Hasan**

- GitHub: [@mehedi77k](https://github.com/mehedi77k)
- Repository: [Library-Management-System](https://github.com/mehedi77k/Library-Management-System)
- Project: Library Management System
- Built with Java

---

## Support

For issues, suggestions, or improvements, open an issue in the GitHub repository:

```text
https://github.com/mehedi77k/Library-Management-System/issues
```

---

## Project Status

```text
Status: Completed
Project Type: Console Application
Language: Java
Programming Concept: Object-Oriented Programming
Database: Not Used
Storage: Runtime / In-memory
```

---

## License

This project is open for educational and learning purposes.

If you want to use a formal open-source license, add a `LICENSE` file to the repository.

---

<div align="center">

### Star this repository if you find it useful.

Made with Java.

</div>
