package com.awais.statistaproxyapi.controller;

import com.awais.statistaproxyapi.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/token")
public class TokenController {
    
    private final JwtUtil jwtUtil;
    
    
   
    public TokenController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    //1. GET /internal/validate - Validates JWT token

    @GetMapping("/validate")
    @Cacheable(value = "tokenValidation", key = "#authHeader")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        System.out.println("Token validation requested");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                        "valid", false, 
                        "message", "No token provided",
                        "timestamp", System.currentTimeMillis()
                    ));
        }
        
        String token = authHeader.substring(7);
        boolean isValid = jwtUtil.validateToken(token);
        
        if (isValid) {
            Claims claims = jwtUtil.extractClaims(token);
            System.out.println("Token valid for user: " + claims.getSubject());
            
            return ResponseEntity.ok(Map.of(
                "valid", true,
                "username", claims.getSubject(),
                "role", claims.get("role", String.class),
                "expiresAt", claims.getExpiration().getTime(),
                "timestamp", System.currentTimeMillis()
            ));
        } else {
            System.out.println("Token invalid or expired");
            return ResponseEntity.status(401)
                    .body(Map.of(
                        "valid", false, 
                        "message", "Token is invalid or expired",
                        "timestamp", System.currentTimeMillis()
                    ));
        }
    }


    
    /**
     * 2. POST /login - Generates JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        System.out.println("Login attempt for user: " + username);
        
        // Simple authentication (in real app, use proper authentication)
        if ("admin".equals(username) && "admin123".equals(password)) {
            // Generate token with claims
            Map<String, Object> claims = Map.of(
                "role", "admin",
                "userId", "12345",
                "email", "admin@example.com"
            );
            
            String token = jwtUtil.generateToken(username, claims);
            
            System.out.println("Login successful for: " + username);
            
            return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "token", token,
                "username", username,
                "role", "admin",
                "expiresIn", "1 hour",
                "timestamp", System.currentTimeMillis()
            ));
        } 
        else if ("user".equals(username) && "user123".equals(password)) {
            // Generate token with claims
            Map<String, Object> claims = Map.of(
                "role", "user",
                "userId", "67890",
                "email", "user@example.com"
            );
            
            String token = jwtUtil.generateToken(username, claims);
            
            System.out.println("Login successful for: " + username);
            
            return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "token", token,
                "username", username,
                "role", "user",
                "expiresIn", "1 hour",
                "timestamp", System.currentTimeMillis()
            ));
        }
        else {
            System.out.println("Login failed for: " + username);
            return ResponseEntity.status(401)
                    .body(Map.of(
                        "error", "Unauthorized",
                        "message", "Invalid credentials",
                        "timestamp", System.currentTimeMillis()
                    ));
        }
    }
    
}