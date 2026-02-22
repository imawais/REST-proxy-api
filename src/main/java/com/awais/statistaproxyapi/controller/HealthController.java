package com.awais.statistaproxyapi.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/internal/health")
public class HealthController {
    
	// Open endpoint that retrieve the status of the API
    @GetMapping
    @Cacheable(value = "internalhealth", key = "#health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        System.out.println("Health check endpoint called");
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Health API",
            "timestamp", System.currentTimeMillis(),
            "message", "Service is healthy"
        ));
    }
}