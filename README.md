# SportHub Booking API

[![Maven CI](https://github.com/Wytse-Maker/sporthub-booking-api/actions/workflows/maven-ci.yml/badge.svg)](https://github.com/Wytse-Maker/sporthub-booking-api/actions/workflows/maven-ci.yml)

SportHub Booking API is a backend portfolio project for booking tickets for NBA sport events.

The project is built with Java, Spring Boot, PostgreSQL and follows a hexagonal architecture approach. The goal of this project is to demonstrate clean backend development, business logic, REST API design, database persistence, validation, exception handling, testing, API documentation, CI automation and environment based configuration.

---

## Project Goal

The goal of this project is to build a clean and maintainable backend API where users can:

- View available sport events
- Create bookings for sport events
- Retrieve bookings
- Retrieve bookings by user
- Cancel bookings
- Receive clear validation and error responses
- Explore and test the API through Swagger/OpenAPI
- Run automated tests through GitHub Actions CI

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
- springdoc-openapi
- Swagger UI
- Git and GitHub
- GitHub Actions

---

## Architecture

This project follows the principles of hexagonal architecture.

Hexagonal architecture separates the inside of the application from the outside world.

The main idea is:

```text
Business logic should not depend directly on frameworks, databases or controllers.
```

This makes the application easier to test, easier to maintain and easier to change later.

The project is divided into two main parts:

- Inner part
- Outer part

---

## Inner Part

The inner part contains the core business logic.

It includes:

- Domain models
- Input ports
- Output ports
- Use case services
- Business exceptions

The inner part does not depend directly on Spring MVC, JPA or PostgreSQL.

### Domain Models

The domain models represent the core business objects of the application.

Important domain classes:

- `Booking`
- `SportEvent`
- `User`
- `Team`
- `Venue`
- `BookingStatus`

These classes describe the business concepts of the application.

Example:

A `Booking` contains:

- User
- Sport event
- Number of tickets
- Booking date
- Booking status

The domain model stays clean and does not contain JPA annotations such as `@Entity`.

---

## Input Ports

Input ports define what the application can do.

They are interfaces used by the outside world, such as REST controllers.

Important input ports:

- `CreateBookingUseCase`
- `CancelBookingUseCase`
- `GetBookingUseCase`
- `GetSportEventsUseCase`

Example:

```text
public interface CreateBookingUseCase {
    Booking createBooking(Long userId, Long sportEventId, Integer numberOfTickets);
}
```

This means that the application offers the ability to create a booking, without exposing how that booking is created internally.

---

## Output Ports

Output ports define what the application needs from the outside world.

They are interfaces used by the application layer to communicate with persistence, without knowing the technical database implementation.

Important output ports:

- `BookingRepositoryPort`
- `SportEventRepositoryPort`
- `UserRepositoryPort`

Example:

```text
public interface BookingRepositoryPort {

    Booking save(Booking booking);

    Optional<Booking> findById(Long bookingId);

    List<Booking> findByUserId(Long userId);

    List<Booking> findActiveBookingsBySportEventId(Long sportEventId);
}
```

The application layer depends on these interfaces, not directly on Spring Data JPA or PostgreSQL.

---

## Application Services

Application services contain the business logic.

Important application services:

- `BookingUseCaseService`
- `SportEventUseCaseService`

### BookingUseCaseService

`BookingUseCaseService` implements:

- `CreateBookingUseCase`
- `CancelBookingUseCase`
- `GetBookingUseCase`

It contains the booking business rules.

Business rules include:

- A booking cannot be created for an event in the past
- The number of tickets must be greater than zero
- A user can book a maximum of 4 tickets per booking
- A booking cannot exceed the available event capacity
- Cancelled bookings do not count toward event capacity
- A booking can only be cancelled up to 24 hours before the event

### SportEventUseCaseService

`SportEventUseCaseService` implements:

- `GetSportEventsUseCase`

It allows the application to:

- Retrieve all sport events
- Retrieve a sport event by ID
- Throw a `ResourceNotFoundException` when a sport event does not exist

---

## Outer Part

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
- Swagger/OpenAPI documentation
- GitHub Actions workflow

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
```

This keeps the controller separated from the database and keeps the business logic clean.

---

## Persistence Layer

The persistence layer connects the application to PostgreSQL.

It contains:

- JPA entities
- Spring Data JPA repositories
- Persistence mappers
- Persistence adapters

---

### JPA Entities

JPA entities represent the database tables.

Important JPA entities:

- `UserJpaEntity`
- `TeamJpaEntity`
- `VenueJpaEntity`
- `SportEventJpaEntity`
- `BookingJpaEntity`

These classes use JPA annotations such as:

```text
@Entity
@Table(name = "bookings")
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@ManyToOne
@JoinColumn(name = "user_id")
@Enumerated(EnumType.STRING)
```

The JPA entities are placed in the infrastructure layer, not in the domain layer.

This keeps the domain independent from database technology.

---

### Spring Data JPA Repositories

Spring Data JPA repositories communicate directly with the database.

Important repositories:

- `SpringDataUserRepository`
- `SpringDataTeamRepository`
- `SpringDataVenueRepository`
- `SpringDataSportEventRepository`
- `SpringDataBookingRepository`

Example:

```text
public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, Long> {
}
```

By extending `JpaRepository`, Spring automatically provides methods such as:

- `findById`
- `findAll`
- `save`
- `deleteById`
- `count`

---

### Persistence Mappers

Persistence mappers convert between domain models and JPA entities.

Important mappers:

- `UserPersistenceMapper`
- `TeamPersistenceMapper`
- `VenuePersistenceMapper`
- `SportEventPersistenceMapper`
- `BookingPersistenceMapper`

Example:

```text
BookingJpaEntity ↔ Booking
SportEventJpaEntity ↔ SportEvent
UserJpaEntity ↔ User
```

This keeps the domain model independent from JPA.

---

### Persistence Adapters

Persistence adapters implement the output ports using Spring Data JPA repositories.

Important adapters:

- `UserPersistenceAdapter`
- `SportEventPersistenceAdapter`
- `BookingPersistenceAdapter`

Example flow:

```text
BookingRepositoryPort
→ BookingPersistenceAdapter
→ SpringDataBookingRepository
→ PostgreSQL
```

This allows the application layer to depend on ports while the infrastructure layer handles the technical implementation.

---

## Web Layer

The web layer exposes the application through REST endpoints.

It contains:

- REST controllers
- DTOs
- Web mappers
- Global exception handler

---

### REST Controllers

Important controllers:

- `SportEventController`
- `BookingController`

These controllers use input ports instead of concrete service classes.

Example:

```text
private final CreateBookingUseCase createBookingUseCase;
```

This keeps the web layer dependent on abstractions rather than concrete implementations.

---

### DTOs

DTO means Data Transfer Object.

DTOs define what data comes in and goes out through the API.

Important DTOs:

- `CreateBookingRequest`
- `BookingResponse`
- `SportEventResponse`
- `ErrorResponse`

DTOs help prevent exposing the internal domain model directly through the API.

---

### Web Mappers

Web mappers convert domain models into response DTOs.

Important web mappers:

- `BookingWebMapper`
- `SportEventWebMapper`

Example:

```text
Booking → BookingResponse
SportEvent → SportEventResponse
```

---

## Spring Beans and Dependency Injection

A Spring Bean is an object managed by Spring.

Spring creates the object, keeps it in the application context and injects it where needed.

This project uses constructor injection.

Example:

```text
public BookingController(
        CreateBookingUseCase createBookingUseCase,
        CancelBookingUseCase cancelBookingUseCase,
        GetBookingUseCase getBookingUseCase
) {
    this.createBookingUseCase = createBookingUseCase;
    this.cancelBookingUseCase = cancelBookingUseCase;
    this.getBookingUseCase = getBookingUseCase;
}
```

This means Spring automatically provides the required dependencies.

---

### UseCaseConfig

The use case services do not use `@Service`.

Instead, they are registered as Spring Beans in:

```text
src/main/java/com/sporthub/booking/infrastructure/config/UseCaseConfig.java
```

This keeps the application layer independent from Spring annotations.

---

## Main Features

### Sport Events

Available endpoints:

```text
GET /api/sport-events
GET /api/sport-events/{sportEventId}
```

These endpoints return sport event data such as:

- Home team
- Away team
- Venue
- Start time
- Ticket price
- Capacity

---

### Bookings

Available endpoints:

```text
POST /api/bookings
GET /api/bookings/{bookingId}
GET /api/bookings/users/{userId}
PATCH /api/bookings/{bookingId}/cancel
```

Users can create, retrieve and cancel bookings.

---

## API Endpoints

### Get all sport events

```text
GET /api/sport-events
```

Returns all available sport events.

---

### Get sport event by ID

```text
GET /api/sport-events/{sportEventId}
```

Returns a single sport event by ID.

---

### Create booking

```text
POST /api/bookings
Content-Type: application/json

{
  "userId": 1,
  "sportEventId": 1,
  "numberOfTickets": 2
}
```

Creates a new booking.

---

### Get booking by ID

```text
GET /api/bookings/{bookingId}
```

Returns a booking by ID.

---

### Get bookings by user ID

```text
GET /api/bookings/users/{userId}
```

Returns all bookings for a specific user.

---

### Cancel booking

```text
PATCH /api/bookings/{bookingId}/cancel
```

Cancels a booking if the event starts more than 24 hours in the future.

---

## Business Rules

The project includes several business rules:

- A booking cannot be created for an event in the past
- The number of tickets must be greater than zero
- A user can book a maximum of 4 tickets per booking
- A booking cannot exceed the available event capacity
- Cancelled bookings do not count toward event capacity
- A booking can only be cancelled up to 24 hours before the event

These rules are implemented in the application use case layer.

---

## Validation

The API validates incoming booking requests.

Example request:

```text
{
  "userId": 1,
  "sportEventId": 1,
  "numberOfTickets": 2
}
```

Validation rules:

- `userId` is required
- `sportEventId` is required
- `numberOfTickets` is required
- `numberOfTickets` must be at least 1
- `numberOfTickets` cannot be greater than 4

Validation annotations are used in `CreateBookingRequest`:

```text
@NotNull
@Min(1)
@Max(4)
```

The controller uses:

```text
@Valid
@RequestBody
```

Invalid requests return a clean `400 Bad Request` response.

---

## Error Handling

The project uses a global exception handler to return clean error responses.

Important class:

```text
GlobalExceptionHandler
```

Important annotations:

```text
@RestControllerAdvice
@ExceptionHandler
```

---

### 404 Not Found

When a booking does not exist:

```text
{
  "status": 404,
  "error": "Not Found",
  "message": "Booking not found with id: 999",
  "timestamp": "2026-07-24T13:00:00"
}
```

---

### 400 Bad Request

When request validation fails:

```text
{
  "status": 400,
  "error": "Bad Request",
  "message": "numberOfTickets: Number of tickets must be at least 1",
  "timestamp": "2026-07-24T13:00:00"
}
```

---

## Database

The project uses PostgreSQL.

Local database name:

```text
sporthub_booking_db
```

Example local configuration:

```text
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/sporthub_booking_db}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:postgres}
```

For real production projects, credentials should be stored in environment variables and not committed directly.

---

## Environment Variables

The project supports environment variables for database configuration.

The application uses default local values when no environment variables are provided.

```text
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/sporthub_booking_db}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:postgres}
spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:update}
```

Supported environment variables:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SPRING_JPA_HIBERNATE_DDL_AUTO
```

Example local values:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/sporthub_booking_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

This makes the project easier to configure in different environments without changing the source code.

---

### Hibernate Configuration

The project uses:

```text
spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:update}
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

This allows Hibernate to create or update database tables during development and show SQL statements in the logs.

---

## Database Seeding

The project includes a `DataSeeder` that automatically inserts development data when the application starts.

Seeded data includes:

- Sample user
- NBA teams
- NBA venues
- Sport events

Example seeded sport events:

- Los Angeles Lakers vs Golden State Warriors
- Golden State Warriors vs Chicago Bulls
- Los Angeles Lakers vs Boston Celtics

The seeder first checks if sport events already exist. If they do, it does not insert duplicate data.

---

## Testing

The project includes unit tests with JUnit 5 and Mockito.

Test coverage includes:

- Getting bookings by ID
- Creating valid bookings
- Rejecting invalid ticket amounts
- Rejecting bookings for past events
- Rejecting bookings when capacity is exceeded
- Cancelling bookings
- Rejecting cancellations within 24 hours of the event
- Handling missing bookings
- Getting sport events
- Handling missing sport events

---

### JUnit 5

JUnit is used to write and run unit tests.

Important annotations:

```text
@Test
@BeforeEach
```

---

### Mockito

Mockito is used to mock dependencies.

Example:

```text
@Mock
private BookingRepositoryPort bookingRepositoryPort;
```

Mockito allows testing the application service without using the real database.

---

## Manual API Testing

The file below contains example API requests:

```text
src/test/http/sporthub-api.http
```

Example request:

```text
POST http://localhost:8080/api/bookings
Content-Type: application/json

{
  "userId": 1,
  "sportEventId": 1,
  "numberOfTickets": 2
}
```

PowerShell can also be used:

```text
Invoke-RestMethod `
  -Uri "http://localhost:8080/api/bookings" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"userId":1,"sportEventId":1,"numberOfTickets":2}'
```

---

## Swagger / OpenAPI Documentation

The project includes Swagger/OpenAPI documentation using springdoc-openapi.

Swagger UI can be used to view and test the REST API endpoints directly in the browser.

After starting the application, open:

```text
http://localhost:8080/swagger-ui.html
```

If that URL does not open, use:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger groups the API endpoints into:

- Sport Events
- Bookings

Available endpoints in Swagger:

```text
GET    /api/sport-events
GET    /api/sport-events/{sportEventId}
POST   /api/bookings
GET    /api/bookings/{bookingId}
GET    /api/bookings/users/{userId}
PATCH  /api/bookings/{bookingId}/cancel
```

Swagger makes it possible to:

- Inspect available endpoints
- Read endpoint descriptions
- See request and response structures
- Test API calls directly from the browser

The OpenAPI metadata is configured in:

```text
src/main/java/com/sporthub/booking/infrastructure/config/OpenApiConfig.java
```

The REST controllers also include Swagger annotations such as:

```text
@Tag
@Operation
```

These annotations improve the readability of the generated API documentation.

---

## Continuous Integration

The project uses GitHub Actions for continuous integration.

The workflow file is located at:

```text
.github/workflows/maven-ci.yml
```

The CI workflow automatically runs when:

- Code is pushed to `main`
- A pull request is opened against `main`

The workflow performs the following steps:

```text
Checkout repository
Set up Java 25
Run Maven tests
```

This helps ensure that changes are tested before they are merged into the main branch.

---

## How to Run the Project Locally

### 1. Clone the repository

```text
git clone <repository-url>
cd sporthub-booking-api
```

---

### 2. Create PostgreSQL database

Create a local PostgreSQL database:

```text
CREATE DATABASE sporthub_booking_db;
```

---

### 3. Configure database connection

The project already contains default local database values.

By default, the application connects to:

```text
jdbc:postgresql://localhost:5432/sporthub_booking_db
```

Optional environment variables can be used to override the default configuration:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SPRING_JPA_HIBERNATE_DDL_AUTO
```

---

### 4. Run tests

```text
mvn test
```

---

### 5. Start the application

```text
mvn spring-boot:run
```

Or start the main class from IntelliJ:

```text
SporthubBookingApiApplication.java
```

---

### 6. Test the API

Open in the browser:

```text
http://localhost:8080/api/sport-events
```

Or open Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

---

## Example API Response

```text
[
  {
    "id": 1,
    "homeTeamName": "Los Angeles Lakers",
    "awayTeamName": "Golden State Warriors",
    "venueName": "Crypto.com Arena",
    "startTime": "2026-08-06T05:29:27",
    "ticketPrice": 89.99,
    "capacity": 20000
  }
]
```

---

## Important Classes

### Domain Layer

- `Booking`
- `SportEvent`
- `User`
- `Team`
- `Venue`
- `BookingStatus`

These classes represent the core business objects.

---

### Input Ports

- `CreateBookingUseCase`
- `CancelBookingUseCase`
- `GetBookingUseCase`
- `GetSportEventsUseCase`

These interfaces define what the application can do.

---

### Output Ports

- `BookingRepositoryPort`
- `SportEventRepositoryPort`
- `UserRepositoryPort`

These interfaces define what the application needs from persistence without depending on a database implementation.

---

### Application Services

- `BookingUseCaseService`
- `SportEventUseCaseService`

These classes contain the business logic.

---

### Persistence Layer

- `UserJpaEntity`
- `TeamJpaEntity`
- `VenueJpaEntity`
- `SportEventJpaEntity`
- `BookingJpaEntity`
- `SpringDataUserRepository`
- `SpringDataTeamRepository`
- `SpringDataVenueRepository`
- `SpringDataSportEventRepository`
- `SpringDataBookingRepository`
- `UserPersistenceMapper`
- `TeamPersistenceMapper`
- `VenuePersistenceMapper`
- `SportEventPersistenceMapper`
- `BookingPersistenceMapper`
- `UserPersistenceAdapter`
- `SportEventPersistenceAdapter`
- `BookingPersistenceAdapter`

This layer connects the application to PostgreSQL.

---

### Web Layer

- `SportEventController`
- `BookingController`
- `CreateBookingRequest`
- `BookingResponse`
- `SportEventResponse`
- `ErrorResponse`
- `BookingWebMapper`
- `SportEventWebMapper`
- `GlobalExceptionHandler`

This layer exposes the API to clients.

---

### Configuration Layer

- `UseCaseConfig`
- `DataSeeder`
- `OpenApiConfig`

This layer configures Spring Beans, seed data and API documentation.

---

### CI Configuration

- `.github/workflows/maven-ci.yml`

This file configures the GitHub Actions workflow that automatically runs the Maven tests.

---

## What This Project Demonstrates

This project demonstrates:

- Java backend development
- Spring Boot REST API development
- Hexagonal architecture
- Clean separation of concerns
- Business rule implementation
- PostgreSQL database integration
- JPA and Hibernate
- DTO usage
- Validation
- Global exception handling
- Unit testing with JUnit and Mockito
- Manual endpoint testing
- Swagger/OpenAPI documentation
- GitHub workflow with feature branches and pull requests
- GitHub Actions CI
- Environment variable based configuration

---

## Project Status

Current status:

- Domain model completed
- Input ports completed
- Output ports completed
- Use cases completed
- Business rules implemented
- Unit tests added
- JPA persistence layer added
- Persistence mappers added
- Persistence adapters added
- REST controllers added
- Global exception handling added
- Request validation added
- PostgreSQL configuration added
- Database seeding added
- Manual API request file added
- README documentation added
- Swagger/OpenAPI documentation added
- Swagger endpoint descriptions added
- GitHub Actions CI workflow added
- CI badge added to README
- Environment variable configuration added
- Environment variable documentation added

---

## Next Possible Improvements

- Add integration tests
- Add Docker support
- Add authentication and authorization
- Add pagination and filtering
- Add more advanced booking rules