## Prerequisites

- **Java 21**
- **Docker** and **Docker Compose**
- **Maven** (or use the included `mvnw` wrapper)

## Getting Started

### Option A: Run with IntelliJ IDEA

Simply run the `RbaApplication` main class. The Spring Boot Docker Compose integration will automatically start the
PostgreSQL container.

### Option B: Run from command line

### 1. Clone and navigate to the project

```bash
cd rba
```

### 2. Start the database

```bash
docker-compose up -d
```

This starts a PostgreSQL database on port 5432.

### 3. Run the application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080/api/v1`

### 4. Run tests

```bash
./mvnw test
```

## Notes

The external Card API (`https://api.something.com`) referenced in the OpenAPI specification does not exist. The
integration is fully implemented and would work if the API were available.

While this API does not include a frontend, I have built a similar frontend application using React and TypeScript,
available at: https://github.com/lpandza/react-app
