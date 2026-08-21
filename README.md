# Student Placement Portal

A Java-based Student Placement Portal that manages student details, company information, eligibility checking, and resume uploads using MySQL.

## Features

- Add student details
- Add company details
- View registered students
- Check student eligibility based on CGPA
- Upload student resumes
- Store resume file paths in MySQL
- MySQL database integration using JDBC
- Console-based menu-driven application

## Technologies Used

- Java
- MySQL
- JDBC
- MySQL Connector/J
- VS Code
- Git & GitHub

## Project Structure

StudentPlacementPortal/
│
├── src/
│   └── PlacementPortal.java
│
├── database.sql
├── README.md
├── .gitignore
└── lib/
    └── MySQL Connector/J

## Database

Database name:

placement_portal

Main tables:

- students
- companies

The `students` table contains:

- id
- name
- branch
- cgpa
- email
- resume_path

## Resume Upload

The application allows students to provide the path of their resume.

Supported formats:

- PDF
- DOC
- DOCX

The resume is copied to the local `resumes` folder, while its file path is stored in the MySQL database.

Personal resumes are excluded from GitHub using `.gitignore`.

## How to Run

### 1. Clone the repository

```bash
git clone https://github.com/Avaneeshkeshpage/Student-Placement-Portal.git
