package com.aasrivas.journalApp.controller;

import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private UserService userService;

    @GetMapping("getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<String> roles = user.getRoles().stream().filter(x -> x.equals("ADMIN")).collect(Collectors.toList());
        if (roles.isEmpty())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<User> allUsers = userService.getAll();
        if (allUsers != null && !allUsers.isEmpty())
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
