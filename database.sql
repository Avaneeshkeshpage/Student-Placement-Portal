CREATE DATABASE placement_portal;
USE placement_portal;

CREATE TABLE students(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    branch VARCHAR(50),
    cgpa DOUBLE,
    email VARCHAR(100)
);

CREATE TABLE companies(
    id INT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(100),
    min_cgpa DOUBLE
);