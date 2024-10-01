package com.ziong.ziong.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    // Constructor Injection of JavaMailSender
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // Method to send order confirmation email
    public void sendOrderConfirmation(String fromEmail, String companyEmail, String subject, String emailContent) {
        try {
            // Create a MimeMessage
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set the "From" address (company email) and "Reply-To" (user email)
            helper.setFrom(companyEmail);  // Company email as the sender
            helper.setTo(companyEmail);    // Send to company or user as needed
            helper.setSubject(subject);    // Email subject
            helper.setText(emailContent);  // Email body/content

            // Set "Reply-To" as the user's email
            helper.setReplyTo(fromEmail);  // User's email for replies

            // Send the email
            javaMailSender.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            System.err.println("Error while sending email: " + e.getMessage());
        }
    }
}
