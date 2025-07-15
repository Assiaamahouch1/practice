package com.fixify.authservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendVerificationEmail(String to, String verificationToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Fixify - Email Verification");
            message.setText(buildVerificationEmailBody(verificationToken));
            
            mailSender.send(message);
            logger.info("Verification email sent to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send verification email to: {}", to, e);
            throw new RuntimeException("Failed to send verification email");
        }
    }
    
    private String buildVerificationEmailBody(String token) {
        return "Welcome to Fixify!\n\n" +
               "Please click the following link to verify your email address:\n" +
               "http://localhost:8080/api/auth/verify-email?token=" + token + "\n\n" +
               "This link will expire in 24 hours.\n\n" +
               "If you didn't create an account with Fixify, please ignore this email.\n\n" +
               "Best regards,\n" +
               "The Fixify Team";
    }
}