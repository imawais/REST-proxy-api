package com.awais.statistaproxyapi.filter;

import com.awais.statistaproxyapi.util.JwtUtil;
import org.springframework.stereotype.Service;


@Service
public class AuthFilter {
    
    private final JwtUtil jwtUtil;
    
    public AuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * Validate authentication - returns true/false
     */
    public boolean validateAuthentication(String proxyHeader, String authHeader) {
        // 1. Check proxy header
        if (proxyHeader == null || !proxyHeader.equals("true")) {
            System.out.println("Missing proxy header");
            return false;
        }
        
        // 2. Check Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Missing Authorization header");
            return false;
        }
        
        // 3. Validate JWT token
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            System.out.println("Invalid JWT token");
            return false;
        }
        
        System.out.println("Authentication successful");
        return true;
    }
    
}
    