# Movie Ticket Booking System

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)

## Features

### User Features

- **User Authentication & Authorization**
  - User registration with email verification
  - Secure login/logout with password encryption
  - Password reset functionality
  - OTP-based email verification
- **Movie Browsing**

  - Browse now showing movies
  - View coming soon movies
  - Search movies by title, genre, or keywords
  - Filter movies by genre
  - Detailed movie information (cast, director, duration, rating, trailer)

- **Ticket Booking**
  - Interactive seat selection
  - Multiple showtime options
  - Date selection for shows
  - Real-time seat availability
- **Additional Services**

  - Food and beverage ordering (popcorn, drinks)
  - Order summary and bill generation
  - Booking history

- **User Profile Management**

  - View and edit profile information
  - Change password
  - View booking history

- **Movie Reviews**
  - Comment on movies
  - View other users' comments

### Admin Features

- Movie management (add, edit, delete)
- User management
- Booking management

## Tech Stack

### Backend

- **Spring Boot 3.3.4** - Application framework
- **Spring Security** - Authentication & authorization (password encryption)
- **Spring Data JPA** - Database ORM
- **Hibernate** - JPA implementation
- **Flyway** - Database migration
- **MySQL 8.0** - Database
- **Spring Mail** - Email service for verification
- **Lombok** - Reduce boilerplate code

### Frontend

- **Thymeleaf** - Server-side template engine
- **HTML5/CSS3** - Markup & styling
- **JavaScript** - Client-side scripting
- **Bootstrap** (via templates) - Responsive design

### Build & Deployment

- **Maven** - Build automation
- **Docker & Docker Compose** - Containerization
- **Java 17** - Programming language

## Project Structure

```
movie-ticket-booking/
├── src/
│   ├── main/
│   │   ├── java/com/example/theater/
│   │   │   ├── configs/          # Security & application configs
│   │   │   ├── controllers/      # REST & MVC controllers
│   │   │   ├── DTOs/              # Data Transfer Objects
│   │   │   ├── entities/          # JPA entities
│   │   │   ├── repositories/      # Database repositories
│   │   │   ├── services/          # Business logic
│   │   │   └── TheaterApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── db/migration/      # Flyway migrations
│   │       ├── static/            # CSS, JS, images
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       └── templates/         # Thymeleaf templates
│   │           ├── fragments/     # Reusable components
│   │           └── *.html         # Page templates
│   └── test/                      # Unit & integration tests
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## API Endpoints

### Public Endpoints

- `GET /` - Homepage
- `GET /login` - Login page
- `POST /login` - Authenticate user
- `GET /register` - Registration page
- `POST /register` - Register new user
- `GET /verify-email` - Email verification with OTP
- `GET /forgot-password` - Password reset request
- `GET /now-showing` - Currently showing movies
- `GET /coming-soon` - Upcoming movies
- `GET /details/{id}` - Movie details
- `GET /search` - Search movies
- `GET /genre/{genre}` - Filter by genre
- `GET /about-us` - About page
- `GET /user-manual` - User manual

### Protected Endpoints (Requires Authentication)

- `GET /booking/{id}` - Booking page for movie
- `POST /booking` - Create booking
- `GET /bill/{id}` - View bill
- `GET /profile` - User profile
- `POST /profile/update` - Update profile
- `GET /change-password` - Change password page
- `POST /change-password` - Update password

### Admin Endpoints (Requires Admin Role)

- `GET /movie-input` - Add/edit movies
- `POST /movie-input` - Save movie
- `DELETE /movie/{id}` - Delete movie
