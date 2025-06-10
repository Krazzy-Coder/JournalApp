package com.aasrivas.journalApp.controller;

import com.aasrivas.journalApp.entity.JournalEntry;
import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.service.JournalEntryService;
import com.aasrivas.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("getAllJournalEntriesOfUser/{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user = userService.findByUserName(userName);
        if (user == null)
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        if (journalEntries.isEmpty())
            return new ResponseEntity<>(user.getJournalEntries(), HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(user.getJournalEntries(), HttpStatus.OK);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> byId = journalEntryService.getById(id);
        if (byId.isPresent())
            return new ResponseEntity<>(byId.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("create/{userName}")
    public ResponseEntity<?> createJournalEntry(@RequestBody JournalEntry entry, @PathVariable String userName) {
        try {
            journalEntryService.createEntry(entry, userName);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("user/{userName}/updateById/{id}")
    public ResponseEntity<?> updateJournalEntryById(@RequestBody Map<String, Object> updates,
                                                    @PathVariable ObjectId id,
                                                    @PathVariable String userName) {
        try {
            journalEntryService.updateById(updates, id, userName);
            return new ResponseEntity<>(HttpStatus.RESET_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_MODIFIED);
        }
    }


    @DeleteMapping("user/{userName}/deleteById/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id, @PathVariable String userName) {
        try {
            journalEntryService.deleteById(id, userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
