package com.fixify.authservice.dto;

import com.fixify.authservice.model.enums.UserRole;
import com.fixify.authservice.model.enums.UserStatus;
import com.fixify.authservice.model.enums.ReparateurStatus;

import java.time.LocalDateTime;

public class UserResponse {
    
    private Long id;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private UserRole role;
    private UserStatus status;
    private boolean emailVerified;
    private boolean phoneVerified;
    private LocalDateTime createdAt;
    
    // For Reparateur specific fields
    private ReparateurStatus reparateurStatus;
    private String speciality;
    
    // Default constructor
    public UserResponse() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public UserStatus getStatus() {
        return status;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    public boolean isEmailVerified() {
        return emailVerified;
    }
    
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
    
    public boolean isPhoneVerified() {
        return phoneVerified;
    }
    
    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public ReparateurStatus getReparateurStatus() {
        return reparateurStatus;
    }
    
    public void setReparateurStatus(ReparateurStatus reparateurStatus) {
        this.reparateurStatus = reparateurStatus;
    }
    
    public String getSpeciality() {
        return speciality;
    }
    
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}