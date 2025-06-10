package com.aasrivas.journalApp.controller;

import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("getAll")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable ObjectId id) {
        Optional<User> byId = userService.getById(id);
        if (byId.isPresent())
            return new ResponseEntity<>(byId.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            if(userService.findByUserName(user.getUserName())!=null)
                    return new ResponseEntity<>("User with given username already present in system. Try different userName.",HttpStatus.NOT_ACCEPTABLE);
            userService.createUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("updateById/{id}")
    public User updateUserById(@RequestBody Map<String, Object> updates,
                                               @PathVariable ObjectId id) {
        return userService.updateById(updates, id);
    }


    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable ObjectId id) {
        boolean isSuccess = userService.deleteById(id);
        if (isSuccess) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
