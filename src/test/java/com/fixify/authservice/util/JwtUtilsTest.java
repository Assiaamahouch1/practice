package com.fixify.authservice.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {
    
    private JwtUtils jwtUtils;
    
    @BeforeEach
    public void setUp() {
        jwtUtils = new JwtUtils();
        
        // Set private fields using reflection for testing
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKeyForJWTTokenGenerationAndValidation2024");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 900000L); // 15 minutes
        ReflectionTestUtils.setField(jwtUtils, "refreshExpirationMs", 604800000L); // 7 days
    }
    
    @Test
    public void testGenerateAccessToken() {
        String username = "test@example.com";
        String role = "CLIENT";
        Long userId = 1L;
        
        String token = jwtUtils.generateAccessToken(username, role, userId);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
    
    @Test
    public void testGenerateRefreshToken() {
        String username = "test@example.com";
        String role = "CLIENT";
        Long userId = 1L;
        
        String token = jwtUtils.generateRefreshToken(username, role, userId);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
    
    @Test
    public void testValidateJwtToken() {
        String username = "test@example.com";
        String role = "CLIENT";
        Long userId = 1L;
        
        String token = jwtUtils.generateAccessToken(username, role, userId);
        
        assertTrue(jwtUtils.validateJwtToken(token));
    }
    
    @Test
    public void testGetUsernameFromJwtToken() {
        String username = "test@example.com";
        String role = "CLIENT";
        Long userId = 1L;
        
        String token = jwtUtils.generateAccessToken(username, role, userId);
        String extractedUsername = jwtUtils.getUsernameFromJwtToken(token);
        
        assertEquals(username, extractedUsername);
    }
    
    @Test
    public void testGetRoleFromJwtToken() {
        String username = "test@example.com";
        String role = "CLIENT";
        Long userId = 1L;
        
        String token = jwtUtils.generateAccessToken(username, role, userId);
        String extractedRole = jwtUtils.getRoleFromJwtToken(token);
        
        assertEquals(role, extractedRole);
    }
    
    @Test
    public void testGetUserIdFromJwtToken() {
        String username = "test@example.com";
        String role = "CLIENT";
        Long userId = 1L;
        
        String token = jwtUtils.generateAccessToken(username, role, userId);
        Long extractedUserId = jwtUtils.getUserIdFromJwtToken(token);
        
        assertEquals(userId, extractedUserId);
    }
    
    @Test
    public void testValidateInvalidJwtToken() {
        String invalidToken = "invalid.token.here";
        
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }
}