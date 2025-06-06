package com.TrungTinhBackend.barbershop_backend.Service.Email;

import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Configuration
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public APIResponse sendEmail(String to, String subject, String body) {
        APIResponse apiResponse = new APIResponse();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Sending mail success !");
        apiResponse.setData(message);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
