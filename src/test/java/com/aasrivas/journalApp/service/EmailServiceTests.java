package com.aasrivas.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {
    @Autowired
    private EmailService emailService;

    @Test
    public void sendMail() {
        emailService.sendEmail("aadeshsrivastava48@gmail.com", "testing", "sncjewkfncejkcnejkcne");
    }

}
