package com.fixify.authservice.model;

import com.fixify.authservice.model.enums.ReparateurStatus;
import com.fixify.authservice.model.enums.UserRole;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "reparateurs")
public class Reparateur extends User {
    
    @Enumerated(EnumType.STRING)
    @Column(name = "reparateur_status", nullable = false)
    private ReparateurStatus reparateurStatus = ReparateurStatus.NON_VERIFIE;
    
    @Column(name = "photo_url")
    private String photoUrl;
    
    @Column(name = "cin")
    private String cin;
    
    @Column(name = "rib")
    private String rib;
    
    @Column(name = "speciality")
    private String speciality;
    
    @Column(name = "experience_years")
    private Integer experienceYears;
    
    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "latitude")
    private Double latitude;
    
    @Column(name = "longitude")
    private Double longitude;
    
    public Reparateur() {
        super();
        setRole(UserRole.REPARATEUR);
    }
    
    public Reparateur(String email, String phone, String password, String firstName, String lastName) {
        super(email, phone, password, firstName, lastName, UserRole.REPARATEUR);
    }
    
    // Getters and Setters
    public ReparateurStatus getReparateurStatus() {
        return reparateurStatus;
    }
    
    public void setReparateurStatus(ReparateurStatus reparateurStatus) {
        this.reparateurStatus = reparateurStatus;
    }
    
    public String getPhotoUrl() {
        return photoUrl;
    }
    
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    
    public String getCin() {
        return cin;
    }
    
    public void setCin(String cin) {
        this.cin = cin;
    }
    
    public String getRib() {
        return rib;
    }
    
    public void setRib(String rib) {
        this.rib = rib;
    }
    
    public String getSpeciality() {
        return speciality;
    }
    
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
    
    public Integer getExperienceYears() {
        return experienceYears;
    }
    
    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }
    
    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }
    
    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}