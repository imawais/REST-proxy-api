package com.awais.statistaproxyapi.controller;

import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class ProxyController {
    
    private final RestTemplate restTemplate;
    
//    private final String baseUrl = "https://jsonplaceholder.typicode.com";
    private final String baseUrl = "http://localhost:8080";
    
    
    public ProxyController() {
        this.restTemplate = new RestTemplate();
    }
    
     //Post endpoints for external api  (no authentication required)
  
//    @GetMapping("/posts")
//    public ResponseEntity<String> posts() {
//        return forwardToInternal("/posts", "GET", null, null);
//    }
    
    /**
     * Health endpoints (no authentication required)
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return forwardToInternal("/internal/health", "GET", null, null);
    }
    
    /**
     * Secure endpoints (require authentication)
     */
    
    // 1. GET /api/subscriptions/data - Get subscriptions (requires JWT)
    @GetMapping("/subscriptions/data")
    public ResponseEntity<String> getSecureData(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return forwardToInternal("/internal/subscriptions/data", "GET", null, authHeader);
    }
    
    
    /**
     * Helper method to forward requests to internal controllers
     */
    private ResponseEntity<String> forwardToInternal(String url, String method, String body, String authHeader) {
        try {
            System.out.println("Forwarding " + method + " to: " + url);
            
            // Create HTTP headers
            HttpHeaders headers = new HttpHeaders();
            
            // Custom HTTP header so secured endpoints should not be accessed directly
            headers.add("proxy-request", "true");

            
            // Forward Authorization header if present
            if (authHeader != null && !authHeader.isEmpty()) {
                headers.set("Authorization", authHeader);
            }
            
            // Set content type for requests
            // Behave as a middleware 
            if (body != null && !body.isEmpty()) {
                headers.setContentType(MediaType.APPLICATION_JSON);
            }
            
            // Create HTTP entity
            HttpEntity<String> entity;
            if (body != null && !body.isEmpty()) {
                entity = new HttpEntity<>(body, headers);
            } else {
                entity = new HttpEntity<>(headers);
            }
            
            // Make the request
            ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + url,
                HttpMethod.valueOf(method),
                entity,
                String.class
            );
            
            System.out.println("Response: " + response.getStatusCode());
            return response;
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to forward request: " + e.getMessage() + "\"}");
        }
    }
}