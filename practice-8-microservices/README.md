# F1 Microservices System

Микросервисная система для управления данными Формулы 1.

## Архитектура

```
                    ┌─────────────────┐
                    │   API Gateway   │ :8080
                    │    (OAuth2)     │
                    └────────┬────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│ Driver Service│   │ Team Service  │   │ Race Service  │
│     :8081     │   │    :8082      │   │    :8083      │
└───────┬───────┘   └───────┬───────┘   └───────┬───────┘
        │                   │                   │
        ▼                   ▼                   ▼
   [PostgreSQL]        [PostgreSQL]        [PostgreSQL]
   drivers_db          teams_db            races_db

                    ┌─────────────────┐
                    │  Auth Service   │ :9000
                    │   (OAuth2 AS)   │
                    └────────┬────────┘
                             │
                        [PostgreSQL]
                         auth_db

Infrastructure:
- Eureka Server :8761 (Service Discovery)
- Config Server :8888 (Centralized Configuration)
```

## Требования

- Java 21+
- Docker & Docker Compose
- Gradle

## Быстрый старт

### 1. Сборка проекта

```bash
# Windows
.\gradlew clean build -x test

# Linux/Mac
./gradlew clean build -x test
```

### 2. Запуск через Docker Compose

```bash
docker-compose up --build
```

### 3. Проверка статуса

- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080/actuator/health

## API Endpoints

### Получение токена (через Gateway)

```bash
curl -X POST http://localhost:8080/oauth2/token \
  -u "f1-client:f1-secret" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&scope=read write"
```

### Примеры запросов

```bash
# Сохраните токен
TOKEN="ваш_access_token"

# Drivers
curl http://localhost:8080/api/drivers -H "Authorization: Bearer $TOKEN"

# Teams  
curl http://localhost:8080/api/teams -H "Authorization: Bearer $TOKEN"

# Races
curl http://localhost:8080/api/races -H "Authorization: Bearer $TOKEN"
```

## Локальная разработка (без Docker)

Запустите сервисы в следующем порядке:

1. PostgreSQL (требуется 4 БД: drivers_db, teams_db, races_db, auth_db)
2. Eureka Server
3. Config Server
4. Auth Service
5. Driver Service, Team Service
6. Race Service
7. API Gateway

## Порты сервисов

| Service        | Port |
|----------------|------|
| API Gateway    | 8080 |
| Driver Service | 8081 |
| Team Service   | 8082 |
| Race Service   | 8083 |
| Auth Service   | 9000 |
| Eureka Server  | 8761 |
| Config Server  | 8888 |

## Остановка

```bash
docker-compose down

# С удалением данных
docker-compose down -v
```

## Технологии

- Spring Boot 3.5
- Spring Cloud 2025.0
- Spring Security OAuth2
- Netflix Eureka
- Spring Cloud Config
- Spring Cloud Gateway
- OpenFeign
- PostgreSQL
- Docker & Docker Compose

