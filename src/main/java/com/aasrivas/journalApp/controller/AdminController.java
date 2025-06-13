package com.aasrivas.journalApp.controller;

import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAll();
        if (allUsers != null && !allUsers.isEmpty())
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("createAdmin")
    public ResponseEntity<?> createAdmin(@RequestBody User user) {
        try {
            if (userService.findByUserName(user.getUserName()) != null)
                return new ResponseEntity<>("User with given username already present in system. Try different userName.", HttpStatus.NOT_ACCEPTABLE);
            userService.createAdmin(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
