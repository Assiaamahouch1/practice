package com.fixify.authservice.exception;

import com.fixify.authservice.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponse response = new ApiResponse(false, "Validation failed", errors);
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        logger.error("Authentication failed: {}", ex.getMessage());
        ApiResponse response = ApiResponse.error("Invalid credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        logger.error("Runtime exception: {}", ex.getMessage());
        
        // Check for specific authentication/authorization errors
        if (ex.getMessage().contains("Invalid credentials") || 
            ex.getMessage().contains("Authentication failed")) {
            ApiResponse response = ApiResponse.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        // Check for user already exists errors
        if (ex.getMessage().contains("already taken") || 
            ex.getMessage().contains("already exists")) {
            ApiResponse response = ApiResponse.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        
        // Check for validation errors
        if (ex.getMessage().contains("required") || 
            ex.getMessage().contains("must be provided")) {
            ApiResponse response = ApiResponse.error(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        
        // Check for not found errors
        if (ex.getMessage().contains("not found")) {
            ApiResponse response = ApiResponse.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
        
        // Check for token expiration errors
        if (ex.getMessage().contains("expired") || 
            ex.getMessage().contains("token")) {
            ApiResponse response = ApiResponse.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        
        // Default to internal server error
        ApiResponse response = ApiResponse.error("An error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unexpected exception: ", ex);
        ApiResponse response = ApiResponse.error("An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}