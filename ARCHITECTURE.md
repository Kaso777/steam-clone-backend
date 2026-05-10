# Architecture

## Overview

Steam Clone Backend is a layered Spring Boot REST API for a Steam-like game library platform. It focuses on authentication, role-based authorization, user/profile management, game catalog management, tags and personal game libraries.

## Package Structure

```text
src/main/java/io/github/kaso777/steamclone/
|-- config/        Spring and security configuration
|-- controller/    REST controllers
|-- dto/           Request and response DTOs
|-- enums/         Application enums
|-- exception/     Custom exceptions
|-- filter/        JWT authentication filter
|-- handler/       Centralized exception handling
|-- model/         JPA entities
|-- repository/    Spring Data JPA repositories
`-- service/       Business logic
```

## Layers

- **Controller layer:** exposes HTTP endpoints and delegates application behavior to services.
- **Service layer:** contains business logic, validation flows and authorization-aware operations.
- **Repository layer:** handles persistence through Spring Data JPA.
- **Security layer:** manages authentication, JWT validation and role-based access rules.

## Security

Authentication is implemented with JWT and Spring Security. Users authenticate through the login endpoint and receive a signed token that must be sent with protected requests.

The application uses two roles:

- `ROLE_USER`
- `ROLE_ADMIN`

Passwords are encoded with BCrypt before persistence. Protected routes are configured through `SecurityConfig`, while endpoint-level permissions use Spring Security annotations where needed.

## Persistence

The application uses MySQL with Spring Data JPA and Hibernate. The domain model includes:

- `User`, with authentication data and role.
- `UserProfile`, linked one-to-one with `User`.
- `Game`, representing catalog entries.
- `Tag`, linked to games through a many-to-many relationship.
- `UserGame`, linking users and games through a composite key for personal library entries.

UUIDs are used for user and game identifiers.

## API Design

The API follows REST conventions with separate DTOs for incoming and outgoing payloads. This keeps persistence entities isolated from external representations and makes validation explicit.

Input validation uses Jakarta Validation annotations such as `@Valid`, `@NotBlank` and `@Size`.

Errors are handled centrally by `GlobalExceptionHandler`, which returns structured responses through `ErrorResponseDTO`.

## Local Runtime

Docker Compose starts both the Spring Boot application and a MySQL 8 container. The database is initialized with `docker/sql/init.sql`, which creates the schema and demo users used by the README and Postman collection.

## Testing

The project includes focused unit tests for DTOs, models and service classes using JUnit 5 and Mockito. Spring Security Test is included for security-related test support.

## Possible Improvements

- Add integration tests with Testcontainers.
- Add OpenAPI/Swagger documentation.
- Add refresh tokens or token revocation strategy.
- Add game reviews and ratings.
- Add payment or order flows for store-like features.
