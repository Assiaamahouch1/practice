package com.fixify.authservice.config;

import com.fixify.authservice.service.EmailService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestConfig {
    
    @Bean
    @Primary
    public EmailService mockEmailService() {
        return new EmailService() {
            @Override
            public void sendVerificationEmail(String to, String verificationToken) {
                // Mock implementation - do nothing for tests
                System.out.println("Mock email sent to: " + to + " with token: " + verificationToken);
            }
        };
    }
}