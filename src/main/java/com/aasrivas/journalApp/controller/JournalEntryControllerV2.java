package com.aasrivas.journalApp.controller;

import com.aasrivas.journalApp.entity.JournalEntry;
import com.aasrivas.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("journal-entry/v2")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("getAllJournalEntries")
    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryService.getAll();
    }

    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> byId = journalEntryService.getById(id);
        if (byId.isPresent())
            return new ResponseEntity<>(byId.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("createEntry")
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry entry) {
        try {
            journalEntryService.createEntry(entry);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournalEntryById(@RequestBody Map<String, Object> updates,
                                               @PathVariable ObjectId id) {
        return journalEntryService.updateById(updates, id);
    }


    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id) {
        boolean isSuccess = journalEntryService.deleteById(id);
        if (isSuccess)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
