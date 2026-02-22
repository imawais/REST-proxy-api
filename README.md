# Statista Proxy API – Case Study Solution

## Overview

This project is a **Spring Boot–based Proxy API** that demonstrates a secure gateway pattern for abstracting complex internal APIs. The proxy provides a simplified interface for clients while handling authentication, authorization, caching, and request forwarding internally.

This architecture improves security, scalability, and maintainability by isolating internal services from direct external access.

---

## Architecture Pattern

- **Proxy/Gateway Pattern**  
  Acts as an intermediary between external clients and internal services.

- **Security Layer**  
  JWT-based authentication with role-based access control.

- **Caching Layer**  
  Spring Cache improves performance for frequently accessed endpoints.

- **RESTful Design**  
  Clean, consistent, and standardized API structure.

---

## Tech Stack & Justification

### Core Technologies

- **Spring Boot 3.x**
  - Production-ready microservice framework
  - Auto-configuration and built-in monitoring
  - Strong ecosystem and community support

- **Java 17**
  - Modern language features
  - Improved performance and stability
  - Long-Term Support (LTS)

- **JJWT (Java JWT)**
  - Secure JWT token creation and validation
  - Stateless authentication support

- **Spring Cache**
  - Performance optimization
  - Reduces load on internal services

---

## Why This Stack?

### Spring Boot
- Rapid development with minimal configuration
- Built-in health checks and metrics
- Industry standard for enterprise microservices

### JWT Authentication
- Stateless and scalable
- No server-side session storage required
- Secure and self-contained authentication tokens

### RESTful Design
- Standardized API structure
- Easy frontend integration
- Clear separation of concerns

---

## Project Structure
com.awais.statistaproxyapi
├── StatistaProxyApiApplication.java
├── controller
│ ├── HealthController.java
│ ├── ProxyController.java
│ ├── SubscriptionController.java
│ └── TokenController.java
├── filter
│ └── AuthFilter.java
└── util
└── JwtUtil.java


### Description

| Component | Description |
|---------|-------------|
| StatistaProxyApiApplication | Main Spring Boot entry point |
| HealthController | Public health check endpoint |
| ProxyController | Handles proxy forwarding |
| SubscriptionController | Protected subscription endpoint |
| TokenController | JWT login and validation |
| AuthFilter | Authentication middleware |
| JwtUtil | JWT creation and validation utilities |

---

## API Endpoints Documentation

### Open Endpoints (No Authentication Required)

| Method | Endpoint | Description |
|-------|---------|-------------|
| GET | `/api/health` | Health check of proxy service |
| POST | `/api/token/login` | Generate JWT token |
| GET | `/api/token/validate` | Validate JWT token |

---

### Protected Endpoints (Require JWT)

| Method | Endpoint | Headers |
|-------|---------|---------|
| GET | `/api/subscriptions/data` | Authorization: Bearer `<token>` |

---

### Internal Endpoints (Proxy Only)

| Method | Endpoint | Description |
|-------|---------|-------------|
| GET | `/internal/health` | Internal service health |
| GET | `/internal/subscriptions/data` | Internal subscription data |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- Spring Boot compatible IDE (IntelliJ, Eclipse, VS Code)

---

### Build the Project

```bash
mvn clean install
