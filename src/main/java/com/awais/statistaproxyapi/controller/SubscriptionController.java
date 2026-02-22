package com.awais.statistaproxyapi.controller;

import com.awais.statistaproxyapi.filter.AuthFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/internal/subscriptions")
public class SubscriptionController {
    
    private final AuthFilter authFilter;
    
    public SubscriptionController(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }
    
    
    // Static array of subscriptions
    private static final List<Map<String, Object>> SUBSCRIPTIONS = List.of(
        Map.of(
            "id", "sub_001",
            "name", "Premium Plan",
            "price", "$29.99/month",
            "features", List.of("Unlimited Access", "Priority Support", "Advanced Analytics"),
            "status", "active",
            "createdAt", "2024-01-15"
        ),
        Map.of(
            "id", "sub_002", 
            "name", "Basic Plan",
            "price", "$9.99/month",
            "features", List.of("Limited Access", "Email Support", "Basic Analytics"),
            "status", "active",
            "createdAt", "2024-01-20"
        ),
        Map.of(
            "id", "sub_003",
            "name", "Enterprise Plan",
            "price", "$99.99/month",
            "features", List.of("Everything", "24/7 Support", "Custom Solutions", "API Access"),
            "status", "active",
            "createdAt", "2024-01-25"
        )
    );
    
    /**
     * 1. GET /subscriptions/data - Returns list of subscriptions (requires JWT)
     */
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getData(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestHeader(value = "proxy-request", required = false) String proxyHeader) {
    	 // Call authentication filter - returns true/false
        boolean isAuthenticated = authFilter.validateAuthentication(proxyHeader, authHeader);
        
        if (!isAuthenticated) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                        "error", "Unauthorized",
                        "timestamp", System.currentTimeMillis()
                    ));
        }
        
        
        // Return subscriptions data
        return ResponseEntity.ok(Map.of(
            "message", "Subscriptions retrieved successfully",
            "data", SUBSCRIPTIONS,
            "count", SUBSCRIPTIONS.size(),
            "timestamp", System.currentTimeMillis()
        ));
    }
}