# salon_management_system

## Overview
The Salon Management System is a desktop-based application developed in Java, designed to manage the daily operations of a beauty salon. 
The application aims to digitalize appointment scheduling, user management, and service organization while maintaining a clean and modular design.
It follows Object-Oriented Programming principles and integrates a relational database for persistent data storage.

## Technologies Used
- Java
- JavaFX (GUI Development)
- JDBC (Database Connectivity)
- Relational Database (MySQL / SQLite)
- OOP Principles
- Java Collections Framework

## Architecture & Design
The application follows the Model–View–Controller (MVC) architectural 
pattern: 
- Model: Contains entity classes such as User, Stylist, Service, and 
Appointment. 
- View: FXML files defining the graphical user interface. 
- Controller: JavaFX controllers handling user interactions and business 
logic. 
- Repository: Classes responsible for database operations using JDBC. 

## Features
- Secure authentication system with role-based access control 
- Client management (create, update, delete, view)
- Appointment scheduling and validation
- Service management
- Persistent data storage via database integration
- User-friendly graphical interface built with JavaFX

![Customerdashboard](screenshots/customer)

## Database
The system uses a relational database to:
- Store client records
- Manage appointments
- Maintain service data
- Ensure data consistency and validation

## How to Run
1. Clone the repository
2. Configure database connection settings
3. Run the main application class
4. Launch the JavaFX interface

## Project Purpose
This project demonstrates:
- Strong understanding of OOP concepts
- GUI development with JavaFX
- Database integration using JDBC
- Practical implementation of real-world business logic
- Clean and modular code structure

---

Developed as part of academic and personal development to strengthen software engineering skills.
