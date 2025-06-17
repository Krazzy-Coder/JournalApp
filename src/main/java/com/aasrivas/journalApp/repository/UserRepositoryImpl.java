package com.aasrivas.journalApp.repository;

import com.aasrivas.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersForSentimentAnalysis() {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("email").ne(null),
                Criteria.where("email").regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"),
                Criteria.where("sentimentAnalysis").is(true)
        ));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
