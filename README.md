# SportHub Booking API

[![Maven CI](https://github.com/Wytse-Maker/sporthub-booking-api/actions/workflows/maven-ci.yml/badge.svg)](https://github.com/Wytse-Maker/sporthub-booking-api/actions/workflows/maven-ci.yml)

SportHub Booking API is a backend portfolio project for booking tickets for NBA sport events.

The project is built with Java, Spring Boot and PostgreSQL and follows a hexagonal architecture approach. The goal of this project is to demonstrate clean backend development, business logic, REST API design, database persistence, validation, exception handling, testing, API documentation, CI automation, Docker support, pagination, filtering, general search, frontend integration through CORS and environment-based configuration.

---

## Project Goal

The goal of this project is to build a clean and maintainable backend API where users can:

- View available sport events
- View sport events with pagination
- Filter sport events by team name
- Filter sport events by venue city
- Search sport events across teams, venue names and venue cities
- Retrieve detailed information about a sport event
- Navigate from a sport event to its related venue
- Retrieve venue details
- Create bookings for sport events
- Retrieve bookings
- Retrieve bookings by user
- Cancel bookings
- Receive clear validation and error responses
- Explore and test the API through Swagger/OpenAPI
- Run automated tests through GitHub Actions CI
- Run the application with Docker and PostgreSQL
- Connect a browser-based frontend through configured CORS rules

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
- Docker
- Docker Compose
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
- `PagedResult`

These classes describe the business concepts of the application.

Example:

A `Booking` contains:

- User
- Sport event
- Number of tickets
- Booking date
- Booking status

A `SportEvent` contains references to:

- Home team
- Away team
- Venue
- Start time
- Ticket price
- Capacity

A `Venue` contains:

- ID
- Name
- City
- Capacity

A `PagedResult` contains:

- Content
- Current page
- Page size
- Total elements
- Total pages
- Last page indicator

The domain model stays clean and does not contain JPA annotations such as `@Entity`.

The application uses its own `PagedResult` instead of Spring Data `Page` in the domain layer. This keeps the core application independent from Spring-specific classes.

---

## Input Ports

Input ports define what the application can do.

They are interfaces used by the outside world, such as REST controllers.

Important input ports:

- `CreateBookingUseCase`
- `CancelBookingUseCase`
- `GetBookingUseCase`
- `GetSportEventsUseCase`
- `GetVenueUseCase`

Example:

```text
public interface CreateBookingUseCase {
    Booking createBooking(Long userId, Long sportEventId, Integer numberOfTickets);
}
```

The venue use case provides:

```text
public interface GetVenueUseCase {
    Venue getVenueById(Long venueId);
}
```

Input ports allow the web layer to use application functionality without depending directly on concrete service implementations.

---

## Output Ports

Output ports define what the application needs from the outside world.

They are interfaces used by the application layer to communicate with persistence without knowing the technical database implementation.

Important output ports:

- `BookingRepositoryPort`
- `SportEventRepositoryPort`
- `UserRepositoryPort`
- `VenueRepositoryPort`

Example:

```text
public interface BookingRepositoryPort {

    Booking save(Booking booking);

    Optional<Booking> findById(Long bookingId);

    List<Booking> findByUserId(Long userId);

    List<Booking> findActiveBookingsBySportEventId(Long sportEventId);
}
```

The venue repository port currently contains only the functionality required by the venue detail use case:

```text
public interface VenueRepositoryPort {

    Optional<Venue> findById(Long venueId);
}
```

The application layer depends on these interfaces, not directly on Spring Data JPA or PostgreSQL.

---

## Application Services

Application services contain the application logic and coordinate the domain with repository ports.

Important application services:

- `BookingUseCaseService`
- `SportEventUseCaseService`
- `VenueUseCaseService`

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

- Retrieve sport events with pagination
- Filter sport events by team name
- Filter sport events by venue city
- Search sport events by home team, away team, venue name or venue city
- Retrieve a sport event by ID
- Throw a `ResourceNotFoundException` when a sport event does not exist

### VenueUseCaseService

`VenueUseCaseService` implements:

- `GetVenueUseCase`

It allows the application to retrieve a venue by ID.

The service uses `VenueRepositoryPort` instead of directly depending on Spring Data JPA.

Example flow:

```text
getVenueById(1)
→ VenueRepositoryPort.findById(1)
→ Venue returned
```

If the venue does not exist:

```text
getVenueById(999)
→ Optional.empty()
→ ResourceNotFoundException
```

This exception is later converted into a `404 Not Found` response by the global exception handler.

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
- Docker configuration
- CORS configuration for frontend integration

The outer part connects the outside world to the inner business logic.

---

## Request Flow Examples

### Creating a booking

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

### Retrieving paginated and filtered sport events

```text
Client
→ GET /api/sport-events?page=0&size=10&team=Lakers
→ SportEventController
→ GetSportEventsUseCase
→ SportEventUseCaseService
→ SportEventRepositoryPort
→ SportEventPersistenceAdapter
→ SpringDataSportEventRepository
→ PostgreSQL
```

### Retrieving venue details

```text
Client
→ GET /api/venues/1
→ VenueController
→ GetVenueUseCase
→ VenueUseCaseService
→ VenueRepositoryPort
→ VenuePersistenceAdapter
→ SpringDataVenueRepository
→ PostgreSQL
```

The domain `Venue` is then converted into a `VenueResponse` by `VenueWebMapper`.

This keeps controllers separated from database technology and keeps the application layer dependent on abstractions.

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

`SpringDataSportEventRepository` also contains a custom query for pagination, filtering and general search.

It supports filtering sport events by:

- Home team name
- Away team name
- Venue city

The general `search` parameter checks:

- Home team name
- Away team name
- Venue name
- Venue city

The search is case-insensitive and supports partial matches.

`SpringDataVenueRepository` provides the persistence functionality used to retrieve a venue by its ID.

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
VenueJpaEntity ↔ Venue
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
- `VenuePersistenceAdapter`

Example booking flow:

```text
BookingRepositoryPort
→ BookingPersistenceAdapter
→ SpringDataBookingRepository
→ PostgreSQL
```

Example venue flow:

```text
VenueRepositoryPort
→ VenuePersistenceAdapter
→ SpringDataVenueRepository
→ PostgreSQL
```

`VenuePersistenceAdapter` retrieves a `VenueJpaEntity` and uses `VenuePersistenceMapper` to convert it into the domain `Venue`.

`SportEventPersistenceAdapter` also converts Spring Data pagination results into the domain-safe `PagedResult`.

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
- `VenueController`

These controllers use input ports instead of concrete service classes.

Example:

```text
private final CreateBookingUseCase createBookingUseCase;
```

The venue controller uses:

```text
private final GetVenueUseCase getVenueUseCase;
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
- `VenueResponse`
- `PagedResponse`
- `ErrorResponse`

DTOs help prevent exposing the internal domain model directly through the API.

`PagedResponse` is used to return paginated API responses to clients.

`VenueResponse` contains:

```text
id
name
city
capacity
```

---

### SportEvent Navigation IDs

`SportEventResponse` includes both display information and IDs for related entities.

Example:

```text
id
homeTeamId
homeTeamName
awayTeamId
awayTeamName
venueId
venueName
startTime
ticketPrice
capacity
```

The names are useful for displaying information to the user.

The IDs allow a frontend application to navigate to related resources.

For example:

```text
Sport event
→ venueId = 1
→ GET /api/venues/1
→ Venue detail screen
```

This avoids having to identify related resources by name.

---

### Web Mappers

Web mappers convert domain models into response DTOs.

Important web mappers:

- `BookingWebMapper`
- `SportEventWebMapper`
- `VenueWebMapper`

Examples:

```text
Booking → BookingResponse
SportEvent → SportEventResponse
Venue → VenueResponse
```

`VenueWebMapper` converts:

```text
Venue
↓
VenueResponse
```

with:

```text
id
name
city
capacity
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

The configured use case services include:

- `BookingUseCaseService`
- `SportEventUseCaseService`
- `VenueUseCaseService`

Example:

```text
@Bean
public VenueUseCaseService venueUseCaseService(
        VenueRepositoryPort venueRepositoryPort
) {
    return new VenueUseCaseService(venueRepositoryPort);
}
```

Spring injects the `VenuePersistenceAdapter` as the implementation of `VenueRepositoryPort`.

---

## Main Features

### Sport Events

Available endpoints:

```text
GET /api/sport-events
GET /api/sport-events?page=0&size=10
GET /api/sport-events?team=Lakers
GET /api/sport-events?city=San%20Francisco
GET /api/sport-events?search=Lakers
GET /api/sport-events?search=crypto
GET /api/sport-events/{sportEventId}
```

These endpoints return sport event data such as:

- Home team ID
- Home team name
- Away team ID
- Away team name
- Venue ID
- Venue name
- Start time
- Ticket price
- Capacity

The sport event list endpoint supports:

- Pagination
- Filtering by team name
- Filtering by venue city
- General search across teams, venue names and venue cities

The related entity IDs allow frontend applications to navigate from a sport event to related data.

---

### Venues

Available endpoint:

```text
GET /api/venues/{venueId}
```

This endpoint returns detailed information about a venue.

Example:

```text
GET /api/venues/1
```

Response:

```text
{
  "id": 1,
  "name": "Crypto.com Arena",
  "city": "Los Angeles",
  "capacity": 20000
}
```

This endpoint can be used by frontend applications to provide a dedicated venue detail screen.

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

### Get sport events

```text
GET /api/sport-events
```

Returns sport events with pagination information.

Optional query parameters:

```text
page
size
team
city
search
```

Examples:

```text
GET /api/sport-events?page=0&size=2
GET /api/sport-events?page=1&size=2
GET /api/sport-events?team=Lakers
GET /api/sport-events?team=Warriors
GET /api/sport-events?city=Los%20Angeles
GET /api/sport-events?city=San%20Francisco
GET /api/sport-events?search=Lakers
GET /api/sport-events?search=crypto
GET /api/sport-events?search=los%20angeles
GET /api/sport-events?page=0&size=5&team=Warriors&city=San%20Francisco
```

### General sport event search

The `search` query parameter provides one frontend-friendly search field.

Examples:

```text
GET /api/sport-events?search=Lakers
GET /api/sport-events?search=Warriors
GET /api/sport-events?search=crypto
GET /api/sport-events?search=los%20angeles
```

The backend searches across:

- Home team name
- Away team name
- Venue name
- Venue city

The search is case-insensitive and supports partial matches.

For example:

```text
search=crypto
```

can match:

```text
Crypto.com Arena
```

This allows a frontend to use a single search bar instead of requiring separate search fields for teams and cities.

---

Example response:

```text
{
  "content": [
    {
      "id": 1,
      "homeTeamId": 1,
      "homeTeamName": "Los Angeles Lakers",
      "awayTeamId": 2,
      "awayTeamName": "Golden State Warriors",
      "venueId": 1,
      "venueName": "Crypto.com Arena",
      "startTime": "2026-08-15T13:09:53",
      "ticketPrice": 89.99,
      "capacity": 20000
    }
  ],
  "page": 0,
  "size": 2,
  "totalElements": 3,
  "totalPages": 2,
  "last": false
}
```

---

### Get sport event by ID

```text
GET /api/sport-events/{sportEventId}
```

Returns a single sport event by ID.

The response also contains the IDs of the home team, away team and venue.

---

### Get venue by ID

```text
GET /api/venues/{venueId}
```

Returns a venue by ID.

Example:

```text
GET /api/venues/1
```

Response:

```text
{
  "id": 1,
  "name": "Crypto.com Arena",
  "city": "Los Angeles",
  "capacity": 20000
}
```

If the venue does not exist, the API returns:

```text
404 Not Found
```

Example error:

```text
{
  "status": 404,
  "error": "Not Found",
  "message": "Venue not found with id: 999",
  "timestamp": "..."
}
```

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

The same `ResourceNotFoundException` mechanism is used for missing bookings, sport events and venues.

---

### 404 Not Found

Example when a booking does not exist:

```text
{
  "status": 404,
  "error": "Not Found",
  "message": "Booking not found with id: 999",
  "timestamp": "2026-07-24T13:00:00"
}
```

Example when a venue does not exist:

```text
{
  "status": 404,
  "error": "Not Found",
  "message": "Venue not found with id: 999",
  "timestamp": "..."
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

Example seeded venues:

- Crypto.com Arena
- Chase Center

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
- Getting paginated sport events
- Passing the general search parameter through the sport event use case
- Handling missing sport events
- Getting an existing venue by ID
- Handling a missing venue

---

### VenueUseCaseService Tests

`VenueUseCaseServiceTest` contains tests for both the successful and unsuccessful lookup flows.

Successful flow:

```text
VenueRepositoryPort.findById(1)
→ Optional containing Venue
→ VenueUseCaseService returns Venue
```

Missing venue flow:

```text
VenueRepositoryPort.findById(999)
→ Optional.empty()
→ ResourceNotFoundException
```

Mockito is used so the service can be tested without connecting to PostgreSQL.

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

Venue tests use:

```text
@Mock
private VenueRepositoryPort venueRepositoryPort;
```

Mockito allows testing application services without using the real database.

---

## Manual API Testing

The file below contains example API requests:

```text
src/test/http/sporthub-api.http
```

Example booking request:

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

Sport event pagination and filtering can be tested in the browser:

```text
http://localhost:8080/api/sport-events?page=0&size=2
http://localhost:8080/api/sport-events?team=Lakers
http://localhost:8080/api/sport-events?team=Warriors
http://localhost:8080/api/sport-events?city=Los%20Angeles
http://localhost:8080/api/sport-events?city=San%20Francisco
http://localhost:8080/api/sport-events?search=Lakers
http://localhost:8080/api/sport-events?search=crypto
http://localhost:8080/api/sport-events?search=los%20angeles
```

Venue details can be tested using:

```text
http://localhost:8080/api/venues/1
```

A missing venue can be tested using:

```text
http://localhost:8080/api/venues/999
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
- Venues
- Bookings

Available endpoints in Swagger include:

```text
GET    /api/sport-events
GET    /api/sport-events?page=0&size=10
GET    /api/sport-events?team=Lakers
GET    /api/sport-events?city=San%20Francisco
GET    /api/sport-events?search=Lakers
GET    /api/sport-events?search=crypto
GET    /api/sport-events/{sportEventId}

GET    /api/venues/{venueId}

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

## Frontend Integration and CORS

The API includes global CORS configuration so that a browser-based frontend, such as Expo Web, can communicate with the Spring Boot backend during local development.

The configuration is located in:

```text
src/main/java/com/sporthub/booking/infrastructure/config/WebConfig.java
```

CORS rules are applied to:

```text
/api/**
```

The current local development origins are:

```text
http://localhost:8081
http://localhost:8082
```

Allowed HTTP methods:

```text
GET
POST
PATCH
OPTIONS
```

Request headers are allowed so the frontend can send requests such as JSON booking requests.

Example development setup:

```text
Expo Web
http://localhost:8081
        ↓
Spring Boot API
http://localhost:8080
```

Because the frontend and backend use different ports, the browser treats them as different origins. The CORS configuration allows the frontend origin to access the API.

CORS was manually verified with a request containing:

```text
Origin: http://localhost:8081
```

The API returned:

```text
Access-Control-Allow-Origin: http://localhost:8081
```

Browser preflight requests using `OPTIONS` are also supported for operations such as `POST /api/bookings`.

The current CORS configuration is intended for local development. When the frontend is deployed, the production frontend origin should be added or used instead of the local development origins.

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

The venue use case tests are also automatically executed by this workflow.

---

## Docker Support

The project can also be started with Docker.

Docker makes it possible to run the Spring Boot application and PostgreSQL database together without manually configuring a local database.

The Docker setup includes:

- Spring Boot application container
- PostgreSQL 17 container
- Docker volume for PostgreSQL data
- Environment variables for database configuration

Important Docker files:

```text
Dockerfile
docker-compose.yml
```

### Start the project with Docker

Make sure Docker Desktop is running.

Then run:

```text
docker compose up --build
```

This will:

- Build the Spring Boot application image
- Start the PostgreSQL container
- Start the application container
- Connect the application to PostgreSQL through Docker Compose

After the application has started, open:

```text
http://localhost:8080/api/sport-events
```

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

### Stop the Docker containers

To stop the running containers, press:

```text
Ctrl + C
```

Then run:

```text
docker compose down
```

### PostgreSQL Docker Port

The PostgreSQL container uses port `5432` internally.

On the local machine, it is exposed on:

```text
5433
```

This avoids conflicts with a locally installed PostgreSQL server that may already be using port `5432`.

### Docker Environment Variables

The Docker Compose file provides these environment variables to the application:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/sporthub_booking_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

This allows the application container to connect to the PostgreSQL container.

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

For a clean build and test run:

```text
mvn clean test
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

Sport events:

```text
http://localhost:8080/api/sport-events
```

Venue details:

```text
http://localhost:8080/api/venues/1
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

---

## Example Sport Event API Response

```text
{
  "content": [
    {
      "id": 1,
      "homeTeamId": 1,
      "homeTeamName": "Los Angeles Lakers",
      "awayTeamId": 2,
      "awayTeamName": "Golden State Warriors",
      "venueId": 1,
      "venueName": "Crypto.com Arena",
      "startTime": "2026-08-15T13:09:53",
      "ticketPrice": 89.99,
      "capacity": 20000
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 3,
  "totalPages": 1,
  "last": true
}
```

---

## Example Venue API Response

```text
{
  "id": 1,
  "name": "Crypto.com Arena",
  "city": "Los Angeles",
  "capacity": 20000
}
```

---

## Important Classes and Files

### Domain Layer

- `Booking`
- `SportEvent`
- `User`
- `Team`
- `Venue`
- `BookingStatus`
- `PagedResult`

These classes represent the core business objects.

---

### Input Ports

- `CreateBookingUseCase`
- `CancelBookingUseCase`
- `GetBookingUseCase`
- `GetSportEventsUseCase`
- `GetVenueUseCase`

These interfaces define what the application can do.

---

### Output Ports

- `BookingRepositoryPort`
- `SportEventRepositoryPort`
- `UserRepositoryPort`
- `VenueRepositoryPort`

These interfaces define what the application needs from persistence without depending on a database implementation.

---

### Application Services

- `BookingUseCaseService`
- `SportEventUseCaseService`
- `VenueUseCaseService`

These classes contain the application logic.

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
- `VenuePersistenceAdapter`

This layer connects the application to PostgreSQL.

---

### Web Layer

- `SportEventController`
- `BookingController`
- `VenueController`
- `CreateBookingRequest`
- `BookingResponse`
- `SportEventResponse`
- `VenueResponse`
- `PagedResponse`
- `ErrorResponse`
- `BookingWebMapper`
- `SportEventWebMapper`
- `VenueWebMapper`
- `GlobalExceptionHandler`

This layer exposes the API to clients.

---

### Configuration Layer

- `UseCaseConfig`
- `DataSeeder`
- `OpenApiConfig`
- `WebConfig`

This layer configures Spring Beans, seed data, API documentation and CORS rules for frontend integration.

---

### Tests

Important application service tests include:

- `BookingUseCaseServiceTest`
- `SportEventUseCaseServiceTest`
- `VenueUseCaseServiceTest`

---

### CI Configuration

- `.github/workflows/maven-ci.yml`

This file configures the GitHub Actions workflow that automatically runs the Maven tests.

---

### Docker Configuration

- `Dockerfile`
- `docker-compose.yml`

These files configure the Spring Boot application container and the PostgreSQL database container.

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
- Ports and adapters
- DTO usage
- Web and persistence mapping
- Related-resource navigation using IDs
- Validation
- Global exception handling
- Pagination
- Filtering
- General search across related sport event data
- Paginated API responses
- CORS configuration for browser-based frontend integration
- Unit testing with JUnit and Mockito
- Mocking repository dependencies
- Manual endpoint testing
- Swagger/OpenAPI documentation
- GitHub workflow with feature branches and pull requests
- GitHub Actions CI
- Environment variable based configuration
- Docker and Docker Compose support

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
- Dockerfile added
- Docker Compose support added
- PostgreSQL Docker container configured
- Docker documentation added
- Pagination for sport events added
- Filtering by team added
- Filtering by venue city added
- Paginated response DTO added
- Navigation IDs added to sport event responses
- Venue repository port added
- Venue use case added
- Venue persistence adapter added
- Venue response DTO added
- Venue web mapper added
- Venue detail endpoint added
- Venue use case unit tests added
- General sport event search added
- General search across home team, away team, venue name and venue city added
- CORS configuration added for frontend integration
- Local Expo Web origins allowed for API requests
- CORS GET and preflight requests tested successfully

---

## Next Possible Improvements

- Add integration and controller tests
- Add authentication and authorization
- Add more advanced booking rules
- Deploy the API to a public environment
- Replace local development CORS origins with the deployed frontend origin when the frontend is hosted