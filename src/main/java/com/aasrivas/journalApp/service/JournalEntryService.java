package com.aasrivas.journalApp.service;

import com.aasrivas.journalApp.entity.JournalEntry;
import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void createEntry(JournalEntry journalEntry, String userName) {
        User user = userService.findByUserName(userName);
        journalEntry.setDate(LocalDateTime.now());
        journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(journalEntry);
        userService.saveUser(user);
    }

    public Optional<JournalEntry> getById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public void updateById(Map<String, String> updates, JournalEntry existingEntry) {
        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    existingEntry.setTitle(value);
                    break;
                case "content":
                    existingEntry.setContent(value);
                    break;
            }
        });

        journalEntryRepository.save(existingEntry);
    }

    @Transactional
    public void deleteById(ObjectId id, String userName) {
        User user = userService.findByUserName(userName);
        journalEntryRepository.deleteById(id);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        for (JournalEntry entry : journalEntries) {
            if (entry.getId().equals(id)) {
                user.getJournalEntries().remove(entry);
                break;
            }
        }
        userService.saveUser(user);
    }
}
