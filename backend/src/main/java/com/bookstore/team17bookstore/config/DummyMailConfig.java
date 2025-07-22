package com.bookstore.team17bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

// This configuration class provides a dummy implementation of JavaMailSender
// that logs email messages to the console instead of sending them.
@Configuration
public class DummyMailConfig {
    
// Bean definition for JavaMailSender that logs messages instead of sending them
@Bean
public JavaMailSender javaMailSender() {
    return new JavaMailSenderImpl() {
        // Dummy implementation of send method
        @Override
        public void send(SimpleMailMessage msg) {
            // log to console instead of sending
            System.out.println("[DEV] Pretending to send mail:\n" + msg);
        }
    };
}

}
