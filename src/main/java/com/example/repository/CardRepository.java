package com.example.repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;

import com.example.models.Card;

public class CardRepository {

    private final Map<Long, Card> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public void save(Card card) {
        if (card.getId() == null) {
            card.setId(idGenerator.getAndIncrement());
            card.setCreatedAt(LocalDateTime.now());
        } else {
            card.setUpdatedAt(LocalDateTime.now());
        }
        storage.put(card.getId(), card);
    }

    public Optional<Card> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Card> getAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Card> deleteById(Long id) {
        return Optional.ofNullable(storage.remove(id));
    }
}
