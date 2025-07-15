package com.fixify.authservice.controller;

import com.fixify.authservice.dto.*;
import com.fixify.authservice.security.UserDetailsServiceImpl;
import com.fixify.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register/client")
    public ResponseEntity<ApiResponse> registerClient(@Valid @RequestBody ClientRegistrationRequest request) {
        UserResponse user = authService.registerClient(request);
        return ResponseEntity.ok(ApiResponse.success("Client registered successfully. Please verify your email if provided.", user));
    }
    
    @PostMapping("/register/reparateur")
    public ResponseEntity<ApiResponse> registerReparateur(@Valid @RequestBody ReparateurRegistrationRequest request) {
        UserResponse user = authService.registerReparateur(request);
        return ResponseEntity.ok(ApiResponse.success("Reparateur registered successfully. Please verify your email to complete registration.", user));
    }
    
    @PostMapping("/register/livreur")
    public ResponseEntity<ApiResponse> registerLivreur(@Valid @RequestBody LivreurRegistrationRequest request) {
        UserResponse user = authService.registerLivreur(request);
        return ResponseEntity.ok(ApiResponse.success("Livreur registered successfully. Please verify your email to complete registration.", user));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse authResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        JwtAuthenticationResponse authResponse = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", authResponse));
    }
    
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.success("Email verified successfully"));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsServiceImpl.UserPrincipal userPrincipal = (UserDetailsServiceImpl.UserPrincipal) authentication.getPrincipal();
        
        authService.logout(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsServiceImpl.UserPrincipal userPrincipal = (UserDetailsServiceImpl.UserPrincipal) authentication.getPrincipal();
        
        UserResponse user = authService.getCurrentUser(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("User details retrieved successfully", user));
    }
}