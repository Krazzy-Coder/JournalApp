package com.aasrivas.journalApp.controller;

import com.aasrivas.journalApp.entity.JournalEntry;
import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.service.JournalEntryService;
import com.aasrivas.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("getAll")
    public ResponseEntity<?> getAllJournalEntries() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        if (journalEntries.isEmpty())
            return new ResponseEntity<>(user.getJournalEntries(), HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(user.getJournalEntries(), HttpStatus.OK);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.getById(id);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("create-entry")
    public ResponseEntity<?> createJournalEntry(@RequestBody JournalEntry entry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        journalEntryService.createEntry(entry, userName);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("updateById/{id}")
    public ResponseEntity<?> updateJournalEntryById(@RequestBody Map<String, String> updates,
                                                    @PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.getById(id);
            if (journalEntry.isPresent()) {
                journalEntryService.updateById(updates, journalEntry.get());
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>("Entry not found with id: " + id, HttpStatus.NOT_MODIFIED);
    }


    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.getById(id);
            if (journalEntry.isPresent()) {
                journalEntryService.deleteById(id, userName);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>("Entry not found with id: " + id, HttpStatus.NOT_ACCEPTABLE);
    }
}
