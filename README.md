# Digital Locker Management System

A simple second-year student-level Java project built with Spring Boot, Maven, Thymeleaf and MySQL (XAMPP).

## Features
- User registration and login
- Password hashing using BCrypt
- Upload documents with categories
- View documents belonging to the logged-in user
- Download and delete documents

## Software required
- Java JDK 21
- IntelliJ IDEA
- XAMPP with MySQL
- Internet connection for Maven to download dependencies the first time

## IMPORTANT: Open the correct folder in IntelliJ
1. Extract this ZIP file.
2. Open the extracted folder. It contains a folder named `DigitalLocker`.
3. In IntelliJ IDEA, click **File > Open**.
4. Select the **DigitalLocker** folder (not `src`, not `.idea`, and not an individual Java file).
5. Confirm that the selected folder contains `pom.xml`, then click **Open**.
6. If IntelliJ asks whether to trust the project, choose **Trust Project**.
7. Wait for Maven to import and download dependencies.

The project root must look like this:

```text
DigitalLocker/
├── pom.xml
├── README.md
├── database/
├── uploads/
└── src/
    └── main/
        ├── java/
        └── resources/
```

## Database setup using XAMPP
1. Open XAMPP Control Panel.
2. Start **MySQL**. Apache is not required for this Spring Boot project.
3. Open `http://localhost/phpmyadmin`.
4. Open the SQL tab and run:

```sql
CREATE DATABASE digital_locker;
```

5. Open `src/main/resources/application.properties`.
6. The default settings are for a typical local XAMPP MySQL setup:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/digital_locker?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
```

If your MySQL root account has a password, enter it after `spring.datasource.password=`.

Spring Boot creates the `users` and `documents` tables automatically when the application starts.

## Run the project
1. Make sure XAMPP MySQL is running.
2. In IntelliJ, open `src/main/java/com/digitallocker/DigitalLockerApplication.java`.
3. Click the green Run icon next to `main()`.
4. Wait for the console to show that Spring Boot has started.
5. Open `http://localhost:8080` in your browser.
6. Register a new account, then log in.

## Frontend
The frontend is intentionally simple: plain labels, text boxes, buttons and a basic document table. It uses HTML, CSS and Thymeleaf. The pages are:
- Login page
- Registration page
- Dashboard with upload form and document list

The frontend is served by Spring Boot. Do not open the Thymeleaf HTML files directly from File Explorer.

## File storage
Uploaded files are stored in the local `uploads` folder. MySQL stores document details and the stored file name.

## Common problems
- **Access denied for root:** check the username and password in `application.properties`.
- **Communications link failure:** make sure MySQL is running in XAMPP and that its port is 3306.
- **Port 8080 already in use:** change `server.port=8081` in `application.properties` and visit `http://localhost:8081`.
- **Maven dependencies not loading:** check your internet connection, then reload Maven in IntelliJ.

## Project note
This is a college learning project, not a production-ready secure document vault. It is suitable for local demonstration with sample files. Do not upload real confidential documents. A real deployment needs stronger session security, CSRF protection, HTTPS, strict file validation, malware scanning and encrypted file storage.
