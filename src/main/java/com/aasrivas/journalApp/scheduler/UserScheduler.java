package com.aasrivas.journalApp.scheduler;

import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.repository.UserRepositoryImpl;
import com.aasrivas.journalApp.service.EmailService;
import com.aasrivas.journalApp.service.SentimentAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserScheduler {
    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private EmailService emailService;

//    @Scheduled(cron = "0 0 9 * * SUN")
    @Scheduled(cron = "0 * * * * *")
    public void fetchUsersAndSendSAMail() {
        List<User> usersForSentimentAnalysis = userRepository.getUsersForSentimentAnalysis();
        for (User user : usersForSentimentAnalysis) {
            // take all last 7 days entries join them and then send in getSentiment method
            String mood = sentimentAnalysisService.getSentiment("Felt anxious on Monday, happy by Friday. Spent time with friends, but also stressed.");
            emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", mood);
        }
    }
}
