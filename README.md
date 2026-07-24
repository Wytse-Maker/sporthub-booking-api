# SportHub Booking API

SportHub Booking API is a backend portfolio project for booking tickets for NBA sport events.

The project is built with Java, Spring Boot, PostgreSQL and follows a hexagonal architecture approach. The goal of this project is to demonstrate clean backend development, business logic, REST API design, database persistence, validation, exception handling and testing.

---

## Project Goal

The goal of this project is to build a clean and maintainable backend API where users can:

- View available sport events
- Create bookings for sport events
- Retrieve bookings
- Retrieve bookings by user
- Cancel bookings
- Receive clear validation and error responses

This project was created as a portfolio project to demonstrate junior backend developer skills.

---

## Technologies Used

- Java 25
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Hibernate
- Maven
- JUnit 5
- Mockito
- Jakarta Validation
- Lombok
- Git and GitHub

---

## Architecture

This project follows the principles of hexagonal architecture.

The main idea is that the domain and application logic should stay independent from external technologies such as databases, controllers or frameworks.

The project is divided into an inner part and an outer part.

### Inner Part

The inner part contains the core business logic.

It includes:

- Domain models
- Input ports
- Output ports
- Use case services
- Business exceptions

The inner part does not depend directly on Spring MVC, JPA or PostgreSQL.

### Outer Part

The outer part contains technical details.

It includes:

- REST controllers
- DTOs
- Web mappers
- JPA entities
- Spring Data JPA repositories
- Persistence mappers
- Persistence adapters
- Spring configuration
- Database seeding
- Global exception handling

The outer part connects the outside world to the inner business logic.

---

## Request Flow Example

Example flow for creating a booking:

```text
Client
→ POST /api/bookings
→ BookingController
→ CreateBookingUseCase
→ BookingUseCaseService
→ BookingRepositoryPort
→ BookingPersistenceAdapter
→ SpringDataBookingRepository
→ PostgreSQL