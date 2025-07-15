package com.fixify.authservice.model;

import com.fixify.authservice.model.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client extends User {
    
    public Client() {
        super();
        setRole(UserRole.CLIENT);
    }
    
    public Client(String email, String phone, String password, String firstName, String lastName) {
        super(email, phone, password, firstName, lastName, UserRole.CLIENT);
    }
}