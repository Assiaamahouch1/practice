package com.fixify.authservice.service;

import com.fixify.authservice.model.RefreshToken;
import com.fixify.authservice.model.User;
import com.fixify.authservice.repository.RefreshTokenRepository;
import com.fixify.authservice.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Value("${fixify.jwt.refresh-token-expiration}")
    private long refreshTokenExpirationMs;
    
    public RefreshToken createRefreshToken(User user) {
        // Revoke existing tokens for the user
        refreshTokenRepository.revokeUserTokens(user);
        
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(refreshTokenExpirationMs / 1000);
        
        RefreshToken refreshToken = new RefreshToken(token, user, expiryDate);
        return refreshTokenRepository.save(refreshToken);
    }
    
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
    
    public void revokeByUser(User user) {
        refreshTokenRepository.revokeUserTokens(user);
    }
    
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}