package com.fixify.authservice.model;

import com.fixify.authservice.model.enums.UserRole;
import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends User {
    
    @Column(name = "permissions")
    private String permissions;
    
    @Column(name = "department")
    private String department;
    
    public Admin() {
        super();
        setRole(UserRole.ADMIN);
    }
    
    public Admin(String email, String phone, String password, String firstName, String lastName) {
        super(email, phone, password, firstName, lastName, UserRole.ADMIN);
    }
    
    // Constructor for Super Admin
    public Admin(String email, String phone, String password, String firstName, String lastName, UserRole role) {
        super(email, phone, password, firstName, lastName, role);
    }
    
    // Getters and Setters
    public String getPermissions() {
        return permissions;
    }
    
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
}