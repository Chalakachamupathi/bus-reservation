## Bus Reservation - Overview

This project provides a simple bus ticket reservation API (server) and a terminal-based Java client.

### Features
- 40 seats (10 rows × 4 seats) A..D
- One-way trip A→D and return D→A daily
- Segment pricing:
  - A–B, B–C, C–D: Rs. 50
  - A–C, B–D: Rs. 100
  - A–D: Rs. 150
- REST API (versioned): `/api/v1`
  - `GET /api/v1/availability?origin=...&destination=...&count=...`
  - `POST /api/v1/reservations` (JSON: origin, destination, count, priceConfirmation)
- In-memory inventory; concurrency-safe seat allocation
- Java CLI client for interactive use

### Quick Start
1) Start server
```bash
docker compose up --build -d
curl -i "http://localhost:8080/api/v1/availability?origin=A&destination=B&count=1"
```

2) Run client (interactive)
```bash
cd java-client && mvn clean package
java -DbaseUrl=http://localhost:8080 -jar target/bus-reservation-client-cli.jar
```

### Running Tests
- Server unit tests:
```bash
cd bus-reservation && mvn test
```

- Client end-to-end tests (server must be running):
```bash
cd java-client && mvn clean package
java -DbaseUrl=http://localhost:8080 \
  -cp target/bus-reservation-client-1.0.0.jar:target/deps/* \
  com.example.client.TestRunner
```

### Configuration
- Client base URL precedence:
  1) JVM system property: `-DbaseUrl=...`
  2) Environment variable: `BASE_URL`
  3) `src/main/resources/application.properties` → `baseUrl=http://localhost:8080`

### Docker
- Server only in docker-compose (Tomcat, port 8080)
- Build service image manually:
```bash
docker build -t bus-reservation:latest ./bus-reservation
```

### Suggested Improvements
- Dates/times and schedules; directional pricing
- Cancellations, refunds, idempotency keys
- Database persistence and migrations
- Authentication/authorization, rate limiting, input sanitization
- Observability (logging, metrics, tracing)
- Smarter seat grouping and preferences

