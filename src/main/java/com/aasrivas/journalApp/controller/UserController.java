package com.aasrivas.journalApp.controller;

import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @GetMapping("getAll")
//    public List<User> getAllUsers() {
//        return userService.getAll();
//    }
//
//    @GetMapping("getById/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable ObjectId id) {
//        Optional<User> byId = userService.getById(id);
//        if (byId.isPresent())
//            return new ResponseEntity<>(byId.get(), HttpStatus.OK);
//        else
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//    }

    @PutMapping("update")
    public void updateUser(@RequestBody Map<String, String> updates) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userService.update(updates, userName);
    }


    @DeleteMapping("delete")
    public void deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userService.delete(userName);
    }
}
