package com.example.demo.securityService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    // Stores the verification codes for users temporarily (in real apps, consider using a database)
    private Map<String, String> verificationCodes = new HashMap<>();

    // Method to send verification code
    public void sendVerificationEmail(String toEmail, String username) throws MessagingException {
        String code = generateVerificationCode();
        verificationCodes.put(toEmail, code); // Save the code for later verification

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Email Verification");
        helper.setText("<h3>Hello, " + username + "</h3>"
                + "<p>Your verification code is: <strong>" + code + "</strong></p>"
                + "<p>Please enter this code to complete your registration.</p>", true);

        mailSender.send(message);
    }

    // Method to generate a 6-digit random code
    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    // Method to verify the code entered by the user
    public boolean verifyCode(String email, String codeEntered) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(codeEntered);
    }
}
