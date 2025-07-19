package com.aasrivas.journalApp.scheduler;

import com.aasrivas.journalApp.entity.JournalEntry;
import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.repository.UserRepositoryImpl;
import com.aasrivas.journalApp.service.EmailService;
import com.aasrivas.journalApp.service.SentimentAnalysisService;
import com.aasrivas.journalApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserScheduler {
    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSAMail() {
        List<User> usersForSentimentAnalysis = userRepository.getUsersForSentimentAnalysis();
        for (User user : usersForSentimentAnalysis) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<String> content = getLastWeekContents(journalEntries);
            String emailBody;
            if (!content.isEmpty()) {
                String combinedJournals = String.join(", ", content);
                emailBody = sentimentAnalysisService.getSentiment(combinedJournals);
            } else {
                emailBody = "Please add some journals for weekly sentiment analysis.";
            }
            emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", emailBody);
        }
    }

    public List<String> getLastWeekContents(List<JournalEntry> allEntries) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        return allEntries.stream()
                .filter(entry -> entry.getDate().isAfter(oneWeekAgo))
                .map(JournalEntry::getContent)
                .collect(Collectors.toList());
    }
}
