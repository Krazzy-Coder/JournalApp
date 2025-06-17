package com.aasrivas.journalApp.repository;

import com.aasrivas.journalApp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserRepositoryImplTests {
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    public void getUsersForSentimentAnalysis() {
        List<User> usersForSentimentAnalysis = userRepositoryImpl.getUsersForSentimentAnalysis();
        usersForSentimentAnalysis.forEach(u -> System.out.println(u.getUserName() + u.getEmail()));
    }
}
