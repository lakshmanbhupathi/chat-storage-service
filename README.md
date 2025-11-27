# Chat-Storage-Service
A Spring Boot microservice for storing chat histories for a RAG-based chatbot system

## Features

- **Session Management**: Create, retrieve, rename, delete, and favorite chat sessions.
- **Message Storage**: Store and retrieve chat messages with support for sender types (USER, ASSISTANT) and context.
- **Security**: API Key authentication for all endpoints.
- **Rate Limiting**: Built-in rate limiting to prevent abuse.
- **Documentation**: Swagger/OpenAPI integration.
- **Docker Support**: Easy setup with Docker Compose (PostgreSQL + pgAdmin).

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.0** (Web, Data JPA, Validation)
- **PostgreSQL** (Database)
- **Bucket4j** (Rate Limiting)
- **Lombok**
- **Docker & Docker Compose**

## Setup & Running

### Prerequisites

- Docker and Docker Compose
- Java 21 (for local development without Docker)
- Maven

### Running with Docker

1.  Clone the repository.
2.  Create a `.env` file from `.env.example`:
    ```bash
    cp .env.example .env
    ```
3.  Start the services:
    ```bash
    docker-compose up -d
    ```
    This will start PostgreSQL and pgAdmin.
4.  Run the application:
    ```bash
    ./mvnw spring-boot:run
    ```
    (Or run it from your IDE).

5. For exporting env into variables
    ```bash
   export $(xargs <.env)
   ```

### Configuration

The application is configured via `application.properties` and environment variables.


 `DB_USERNAME`  Database username  `postgres` 
 `DB_PASSWORD`  Database password `password` 
 `DB_NAME`  Database name  `chat_history_db` 
 `APP_API_KEY`  API Key for authentication  `my-secret-api-key` 

## API Documentation

Once the application is running, you can access the Swagger UI at:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Authentication

All API requests must include the `X-API-KEY` header with the value configured in `APP_API_KEY`.

### Endpoints

-   `POST /api/sessions`: Create a new session
-   `GET /api/sessions?userId={userId}`: Get all sessions for a user
-   `GET /api/sessions/{id}`: Get a specific session
-   `PATCH /api/sessions/{id}`: Update session (rename/favorite)
-   `DELETE /api/sessions/{id}`: Delete a session
-   `POST /api/sessions/{id}/messages`: Add a message to a session
-   `GET /api/sessions/{id}/messages`: Get messages for a session (paginated)


### Running Tests

```bash
mvn test
```

### Database Management

Access pgAdmin at [http://localhost:5050](http://localhost:5050).
- **Email**: `admin@example.com` (from .env)
- **Password**: `admin` (from .env)
