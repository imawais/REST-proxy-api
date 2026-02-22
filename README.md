
Statista Proxy API - Case Study Solution

Overview

This is a Spring Boot-based proxy API implementation that demonstrates a secure gateway pattern for abstracting complex internal APIs. The solution provides a simplified interface for clients while handling authentication, authorization, and request forwarding internally.

Architecture Pattern

Proxy/Gateway Pattern: Acts as an intermediary between clients and internal services

Security Layer: JWT-based authentication with role-based access control

Caching: Spring Cache for frequently accessed endpoints

RESTful Design: Clean, consistent API design

Tech Stack & Justification

Core Technologies:

Spring Boot 3.x: Industry standard for microservices, provides production-ready features

Java 17: Modern Java with enhanced performance and features

JJWT (Java JWT): Secure token handling for authentication

Spring Cache: Performance optimization for health checks

Why This Stack?
Spring Boot:

Rapid development with auto-configuration

Built-in security features

Extensive ecosystem and community support

Production-ready with health checks, metrics, etc.

JWT Authentication:

Stateless authentication suitable for microservices

Scalable across multiple instances

Self-contained tokens with expiration

RESTful Design:

Standardized API patterns

Easy integration with frontend applications

Clear separation of concerns

Project Structure

com.awais.statistaproxyapi
├── StatistaProxyApiApplication.java      # Main application class
├── controller
│   ├── HealthController.java             # Open health endpoint
│   ├── ProxyController.java              # Main proxy controller
│   ├── SubscriptionController.java       # Protected subscription data
│   └── TokenController.java              # Token management
├── filter
│   └── AuthFilter.java                   # Authentication middleware
└── util
    └── JwtUtil.java                      # JWT utilities
API Endpoints Documentation

Open Endpoints (No Authentication Required)
Method	Endpoint	Description	Response
GET	/api/health	Health check of the proxy service	JSON with service status
POST	/api/token/login	Generate JWT token	Token and user info
GET	/api/token/validate	Validate JWT token	Token validation status
Protected Endpoints (Require JWT Authentication)
Method	Endpoint	Description	Headers Required
GET	/api/subscriptions/data	Get subscription data	Authorization: Bearer <token>
Internal Endpoints (Called by Proxy)
Method	Endpoint	Description
GET	/internal/health	Internal health check
GET	/internal/subscriptions/data	Internal subscription data

Getting Started

Prerequisites:

Java 17 or higher

Maven 3.6+

Spring Boot 3.x compatible IDE

Running the Application:
Clone and build:

bash
mvn clean install
Run the application:

bash
mvn spring-boot:run
Default port: 8080

Testing the API:
1. Get Health Status
bash
curl -X GET http://localhost:8080/api/health
2. Generate JWT Token
bash
# Admin user
curl -X POST http://localhost:8080/api/token/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Regular user
curl -X POST http://localhost:8080/api/token/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}'
3. Access Protected Resource
bash
curl -X GET http://localhost:8080/api/subscriptions/data \
  -H "Authorization: Bearer <your-jwt-token>"

Key Features

1. Proxy Layer

Routes external requests to internal services

Adds security headers (proxy-request: true)

Handles error responses gracefully

2. Authentication System

JWT-based token generation and validation

Role-based access control

Token expiration (1 hour)

3. Security Features

Header validation (proxy-request header)

JWT token validation

Protection against direct internal access

4. Performance Optimizations

Cached health checks

Efficient JWT validation

Connection pooling via RestTemplate

Security Implementation

Authentication Flow:

Client obtains JWT via /api/token/login

Client includes token in Authorization header

Proxy adds proxy-request: true header

Internal service validates both headers

Access granted/denied based on validation

Security Headers:

Authorization: Bearer <token> - JWT authentication

proxy-request: true - Ensures request came through proxy

Custom validation prevents direct internal access

Infrastructure Considerations

Production Deployment:

yaml
Infrastructure Components:
- Load Balancer (AWS ALB / Nginx)
- Auto-scaling Group (2+ instances)
- Database (RDS / DynamoDB for token blacklist)
- Redis Cluster (for distributed caching)
- CloudWatch / Prometheus (monitoring)
- AWS Secrets Manager (for JWT secrets)

High Availability Setup:

Multiple Availability Zones for fault tolerance

Containerization with Docker and Kubernetes/EKS

CI/CD Pipeline using Jenkins/GitHub Actions

API Gateway (AWS API Gateway or Kong) for additional layer

Security Enhancements:

Rate Limiting to prevent abuse

JWT Blacklisting for token revocation

API Key Management for external partners

WAF Integration for DDoS protection

Testing Credentials

Username	Password	Role	Access Level
admin	admin123	admin	Full access
user	user123	user	Standard access

Monitoring & Observability

Key Metrics to Monitor:

Request latency (P95, P99)

Error rates (4xx, 5xx)

Authentication success/failure rates

Cache hit/miss ratios

JWT token expiration patterns

Health Checks:

Application health: GET /api/health

Database connectivity (if added)

External service dependencies

Future Enhancements

Immediate Improvements:

Add database for dynamic subscription data

Implement rate limiting

Add API versioning

Enhance error handling with custom exceptions

Long-term Features:

WebSocket support for real-time updates

GraphQL endpoint for flexible queries

API documentation with Swagger/OpenAPI

Distributed tracing with Jaeger/Zipkin

Design Decisions

Why Proxy Pattern?

Abstraction: Hides complex internal API details

Security: Centralized authentication and authorization

Flexibility: Easy to modify internal services without affecting clients

Monitoring: Single point for logging and analytics

Why JWT?

Stateless: No server-side session storage needed

Scalable: Works well in distributed systems

Self-contained: All user info in token

Standard: Widely adopted and understood

Limitations & Considerations

Current Limitations:

In-memory authentication (demo purposes only)

No persistent storage for tokens

Basic error handling

Limited to single instance deployment

Production Considerations:

Use secure secret management

Implement token blacklisting

Add comprehensive logging

Set up proper monitoring alerts