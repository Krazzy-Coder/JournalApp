package com.aasrivas.journalApp.service;

import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        user.setRoles(Arrays.asList("USER"));
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> getById(ObjectId id) {
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void update(Map<String, String> updates, String userName) {
        User existingUser = findByUserName(userName);
        updates.forEach((key, value) -> {
            switch (key) {
                case "userName":
                    existingUser.setUserName(value);
                    break;
                case "password":
                    existingUser.setPassword(new BCryptPasswordEncoder().encode(value));
                    break;
            }
        });

        userRepository.save(existingUser);
    }

    public void delete(String userName) {
        User existingUser = findByUserName(userName);
        userRepository.deleteById(existingUser.getId());
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

}
