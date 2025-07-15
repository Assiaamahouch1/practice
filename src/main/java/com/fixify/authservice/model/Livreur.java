package com.fixify.authservice.model;

import com.fixify.authservice.model.enums.UserRole;
import jakarta.persistence.*;

@Entity
@Table(name = "livreurs")
public class Livreur extends User {
    
    @Column(name = "vehicle_type")
    private String vehicleType;
    
    @Column(name = "license_number")
    private String licenseNumber;
    
    @Column(name = "current_latitude")
    private Double currentLatitude;
    
    @Column(name = "current_longitude")
    private Double currentLongitude;
    
    @Column(name = "is_available")
    private boolean isAvailable = true;
    
    @Column(name = "zone_coverage")
    private String zoneCoverage;
    
    public Livreur() {
        super();
        setRole(UserRole.LIVREUR);
    }
    
    public Livreur(String email, String phone, String password, String firstName, String lastName) {
        super(email, phone, password, firstName, lastName, UserRole.LIVREUR);
    }
    
    // Getters and Setters
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    
    public Double getCurrentLatitude() {
        return currentLatitude;
    }
    
    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }
    
    public Double getCurrentLongitude() {
        return currentLongitude;
    }
    
    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    public String getZoneCoverage() {
        return zoneCoverage;
    }
    
    public void setZoneCoverage(String zoneCoverage) {
        this.zoneCoverage = zoneCoverage;
    }
}