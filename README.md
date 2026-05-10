# Steam Clone Backend API

REST backend for a Steam-like game library platform. The application manages users, user profiles, games, tags, personal libraries and role-based access to protected operations.

## Features

- User registration and login with JWT authentication.
- Role-based authorization with `ROLE_USER` and `ROLE_ADMIN`.
- CRUD APIs for users, profiles, games and tags.
- Personal game library management for users.
- Search endpoints by title, tag, developer and publisher.
- DTO-based request/response layer with validation.
- Centralized exception handling with structured error responses.
- MySQL persistence with Spring Data JPA and Hibernate.
- Docker Compose setup for local application and database startup.
- Unit tests with JUnit 5, Mockito and Spring Security Test.
- Postman collection for manual API testing.

## Tech Stack

- Java 21
- Spring Boot 3.5.3
- Spring Web
- Spring Security
- JWT
- Spring Data JPA / Hibernate
- MySQL 8
- Maven
- Docker / Docker Compose
- JUnit 5, Mockito, Spring Security Test
- JaCoCo
- Postman

## Requirements

- JDK 21 or newer
- Docker and Docker Compose
- Maven, or the included Maven wrapper

## Local Configuration

The project includes `.env.example` with demo values for local development.

To customize your local environment:

```bash
cp .env.example .env
```

Then update `.env` as needed. The `.env` file is ignored by Git and must not be committed.

The credentials documented below are local demo users created by `docker/sql/init.sql`.

## Build

```bash
./mvnw clean package
```

On Windows, if the Maven wrapper has path issues, use a local Maven installation:

```bash
mvn clean package
```

## Run With Docker Compose

Start MySQL and the Spring Boot application:

```bash
docker compose up --build -d
```

The API will be available at:

```text
http://localhost:8080
```

The first startup initializes the database using `docker/sql/init.sql`.

## Demo Users

| Role | Username | Password | ID |
| --- | --- | --- | --- |
| Admin | `admin` | `adminpass` | `11111111-1111-1111-1111-111111111111` |
| User | `user` | `userpass` | `22222222-2222-2222-2222-222222222222` |

Only `login` and `register` are public. Other `/api/**` routes require authentication. Admin-only operations are protected with role checks.

## Postman

The collection is available in:

```text
postman/SteamClone API.postman_collection.json
```

Suggested manual flow:

1. Import the collection in Postman.
2. Run `Auth > User Login` with one of the demo users.
3. Copy the returned JWT into the `ADMIN_token` or `USER_token` collection variable.
4. Call the protected endpoints.

## Tests

Run the full verification phase:

```bash
./mvnw clean verify
```

Or:

```bash
mvn clean verify
```

JaCoCo generates the HTML report at:

```text
target/site/jacoco/index.html
```

## Architecture

See [ARCHITECTURE.md](ARCHITECTURE.md) for package structure, design choices and main application layers.
