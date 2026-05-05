# Book Swap & Lending Platform

Production-ready Spring Boot backend scaffold for a Book Swap & Lending Platform.

## Tech Stack

- Java 17
- Spring Boot 3.5.0
- Maven
- PostgreSQL
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring Mail
- Spring WebSocket

## Project Structure

- `controller` - REST API endpoints
- `service` - Business service contracts
- `service.impl` - Service implementations
- `repository` - JPA repositories
- `model` - JPA entities and enums
- `dto` - Request/response payload models
- `config` - Auditing and websocket configuration
- `security` - Security and user details integration
- `exception` - Global exception handling and error payloads

## Run Locally

1. Create PostgreSQL database:

```sql
CREATE DATABASE bookswap_db;
```

2. Update credentials in `src/main/resources/application.properties`.
3. Run the app:

```bash
mvn spring-boot:run
```
