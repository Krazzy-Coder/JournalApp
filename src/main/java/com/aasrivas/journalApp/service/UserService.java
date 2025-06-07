package com.aasrivas.journalApp.service;

import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> getById(ObjectId id) {
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

//    public User updateById(Map<String, Object> updates, ObjectId id) {
//        User existingUser = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        updates.forEach((key, value) -> {
//            switch (key) {
//                case "title":
//                    existingUser.setUserName((String) value);
//                    break;
//                case "content":
//                    existingUser.setPassword((String) value);
//                    break;
//                // ignore unknown fields silently or throw exception if strict validation is needed
//            }
//        });
//
//        return userRepository.save(existingUser);
//    }

    public boolean deleteById(ObjectId id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
