package com.aasrivas.journalApp.service;

import com.aasrivas.journalApp.entity.JournalEntry;
import com.aasrivas.journalApp.entity.User;
import com.aasrivas.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void createEntry(JournalEntry journalEntry, String userName) {
        User user = userService.findByUserName(userName);
        if (user == null)
            throw new RuntimeException("User not found");
        journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(journalEntry);
        userService.createUser(user);
    }

    public Optional<JournalEntry> getById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public void updateById(Map<String, Object> updates, ObjectId id, String userName) {
        User user = userService.findByUserName(userName);
        if (user == null)
            throw new RuntimeException("User not found");
        JournalEntry existingEntry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "title":
                    existingEntry.setTitle((String) value);
                    break;
                case "content":
                    existingEntry.setContent((String) value);
                    break;
            }
        });

        journalEntryRepository.save(existingEntry);
    }

    @Transactional
    public void deleteById(ObjectId id, String userName) {
        User user = userService.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User doesn't exist.");
        }
        journalEntryRepository.deleteById(id);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        boolean entryFound = false;
        for (JournalEntry entry : journalEntries) {
            if (entry.getId().equals(id)) {
                user.getJournalEntries().remove(entry);
                entryFound = true;
                break;
            }
        }
        if (!entryFound)
            throw new RuntimeException("Journal entry with id: " + id + " doesn't exist for user: " + userName);
        userService.createUser(user);
    }

}
