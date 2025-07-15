package com.fixify.authservice.integration;

import com.fixify.authservice.config.TestConfig;
import com.fixify.authservice.dto.ClientRegistrationRequest;
import com.fixify.authservice.dto.LoginRequest;
import com.fixify.authservice.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
public class AuthControllerIntegrationTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
    
    @Test
    public void testClientRegistration() {
        ClientRegistrationRequest request = new ClientRegistrationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        
        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                createURLWithPort("/api/auth/register/client"), 
                request, 
                ApiResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
    }
    
    @Test
    public void testClientRegistrationWithPhone() {
        ClientRegistrationRequest request = new ClientRegistrationRequest();
        request.setPhone("+1234567890");
        request.setPassword("password123");
        request.setFirstName("Jane");
        request.setLastName("Doe");
        
        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                createURLWithPort("/api/auth/register/client"), 
                request, 
                ApiResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
    }
    
    @Test
    public void testClientRegistrationValidation() {
        ClientRegistrationRequest request = new ClientRegistrationRequest();
        // Missing required fields
        
        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                createURLWithPort("/api/auth/register/client"), 
                request, 
                ApiResponse.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }
    
    @Test
    public void testLogin() {
        // First register a client
        ClientRegistrationRequest regRequest = new ClientRegistrationRequest();
        regRequest.setEmail("login@example.com");
        regRequest.setPassword("password123");
        regRequest.setFirstName("Login");
        regRequest.setLastName("Test");
        
        restTemplate.postForEntity(
                createURLWithPort("/api/auth/register/client"), 
                regRequest, 
                ApiResponse.class);
        
        // Now try to login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailOrPhone("login@example.com");
        loginRequest.setPassword("password123");
        
        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                createURLWithPort("/api/auth/login"), 
                loginRequest, 
                ApiResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
    }
}