-- create database
CREATE DATABASE IF NOT EXISTS studentdb;
USE studentdb;

-- create students table
CREATE TABLE IF NOT EXISTS students (
  id INT AUTO_INCREMENT PRIMARY KEY,
  roll_no VARCHAR(50) NOT NULL UNIQUE,
  name VARCHAR(150) NOT NULL,
  email VARCHAR(150),
  branch VARCHAR(100),
  year INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- create users table for simple login (one admin user)
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  password_hash VARCHAR(200) NOT NULL
);

-- insert a default admin user (password: admin123) -- demo only
INSERT IGNORE INTO users (username, password_hash)
VALUES ('admin', 'admin123');
