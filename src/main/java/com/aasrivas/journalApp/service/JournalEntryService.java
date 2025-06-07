package com.aasrivas.journalApp.service;

import com.aasrivas.journalApp.entity.JournalEntry;
import com.aasrivas.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public void createEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public Optional<JournalEntry> getById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry updateById(Map<String, Object> updates, ObjectId id) {
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
                // ignore unknown fields silently or throw exception if strict validation is needed
            }
        });

        return journalEntryRepository.save(existingEntry);
    }

    public boolean deleteById(ObjectId id) {
        try {
            journalEntryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
