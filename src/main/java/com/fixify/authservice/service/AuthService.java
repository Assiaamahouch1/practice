package com.fixify.authservice.service;

import com.fixify.authservice.dto.*;
import com.fixify.authservice.model.*;
import com.fixify.authservice.model.enums.UserRole;
import com.fixify.authservice.model.enums.UserStatus;
import com.fixify.authservice.repository.UserRepository;
import com.fixify.authservice.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    public UserResponse registerClient(ClientRegistrationRequest request) {
        // Validate that either email or phone is provided
        if (request.getEmail() == null && request.getPhone() == null) {
            throw new RuntimeException("Either email or phone must be provided");
        }
        
        // Check if user already exists
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }
        
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number is already taken!");
        }
        
        // Create new client
        Client client = new Client();
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setStatus(UserStatus.PENDING_VERIFICATION);
        
        // Generate verification token if email is provided
        if (request.getEmail() != null) {
            String verificationToken = UUID.randomUUID().toString();
            client.setVerificationToken(verificationToken);
            client.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
        }
        
        User savedUser = userRepository.save(client);
        
        // Send verification email if email is provided
        if (request.getEmail() != null) {
            try {
                emailService.sendVerificationEmail(request.getEmail(), client.getVerificationToken());
            } catch (Exception e) {
                logger.error("Failed to send verification email", e);
                // Continue with registration even if email fails
            }
        }
        
        return mapToUserResponse(savedUser);
    }
    
    public UserResponse registerReparateur(ReparateurRegistrationRequest request) {
        // Email is required for reparateur
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required for reparateur registration");
        }
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }
        
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number is already taken!");
        }
        
        // Create new reparateur
        Reparateur reparateur = new Reparateur();
        reparateur.setEmail(request.getEmail());
        reparateur.setPhone(request.getPhone());
        reparateur.setPassword(passwordEncoder.encode(request.getPassword()));
        reparateur.setFirstName(request.getFirstName());
        reparateur.setLastName(request.getLastName());
        reparateur.setStatus(UserStatus.PENDING_VERIFICATION);
        reparateur.setSpeciality(request.getSpeciality());
        reparateur.setExperienceYears(request.getExperienceYears());
        reparateur.setHourlyRate(request.getHourlyRate());
        reparateur.setDescription(request.getDescription());
        reparateur.setAddress(request.getAddress());
        reparateur.setCity(request.getCity());
        
        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();
        reparateur.setVerificationToken(verificationToken);
        reparateur.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
        
        User savedUser = userRepository.save(reparateur);
        
        // Send verification email
        try {
            emailService.sendVerificationEmail(request.getEmail(), reparateur.getVerificationToken());
        } catch (Exception e) {
            logger.error("Failed to send verification email", e);
        }
        
        return mapToUserResponse(savedUser);
    }
    
    public UserResponse registerLivreur(LivreurRegistrationRequest request) {
        // Email is required for livreur
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required for livreur registration");
        }
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }
        
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number is already taken!");
        }
        
        // Create new livreur
        Livreur livreur = new Livreur();
        livreur.setEmail(request.getEmail());
        livreur.setPhone(request.getPhone());
        livreur.setPassword(passwordEncoder.encode(request.getPassword()));
        livreur.setFirstName(request.getFirstName());
        livreur.setLastName(request.getLastName());
        livreur.setStatus(UserStatus.PENDING_VERIFICATION);
        livreur.setVehicleType(request.getVehicleType());
        livreur.setLicenseNumber(request.getLicenseNumber());
        livreur.setZoneCoverage(request.getZoneCoverage());
        
        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();
        livreur.setVerificationToken(verificationToken);
        livreur.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
        
        User savedUser = userRepository.save(livreur);
        
        // Send verification email
        try {
            emailService.sendVerificationEmail(request.getEmail(), livreur.getVerificationToken());
        } catch (Exception e) {
            logger.error("Failed to send verification email", e);
        }
        
        return mapToUserResponse(savedUser);
    }
    
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmailOrPhone(),
                            loginRequest.getPassword()
                    )
            );
            
            // Get user details
            String username = authentication.getName();
            User user = userRepository.findByEmailOrPhone(username, username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Generate tokens
            String accessToken = jwtUtils.generateAccessToken(username, user.getRole().name(), user.getId());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
            
            UserResponse userResponse = mapToUserResponse(user);
            
            return new JwtAuthenticationResponse(accessToken, refreshToken.getToken(), userResponse);
            
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }
    
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateAccessToken(user.getLoginIdentifier(), 
                            user.getRole().name(), user.getId());
                    UserResponse userResponse = mapToUserResponse(user);
                    return new JwtAuthenticationResponse(token, requestRefreshToken, userResponse);
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
    
    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));
        
        if (user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired");
        }
        
        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        
        userRepository.save(user);
    }
    
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        refreshTokenService.revokeByUser(user);
    }
    
    public UserResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return mapToUserResponse(user);
    }
    
    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setEmailVerified(user.isEmailVerified());
        response.setPhoneVerified(user.isPhoneVerified());
        response.setCreatedAt(user.getCreatedAt());
        
        // Add reparateur specific fields if applicable
        if (user instanceof Reparateur) {
            Reparateur reparateur = (Reparateur) user;
            response.setReparateurStatus(reparateur.getReparateurStatus());
            response.setSpeciality(reparateur.getSpeciality());
        }
        
        return response;
    }
}