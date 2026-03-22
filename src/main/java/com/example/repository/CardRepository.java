package com.example.repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;

import com.example.models.Card;
import com.example.models.CardStatus;

public class CardRepository {

    private final Map<Long, Card> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public CardRepository() {
        initStorage();
    }

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

    private void initStorage() {
        this.save(Card.builder()
                .cardNumber("1234-5678-9012-3456")
                .cardHolderName("IVAN PETROV")
                .expirationDate("2026-12-31")
                .status(CardStatus.ACTIVE)
                .accountId(1001L)
                .build());

        this.save(Card.builder()
                .cardNumber("2345-6789-0123-4567")
                .cardHolderName("MARIA SOKOLOVA")
                .expirationDate("2025-06-30")
                .status(CardStatus.ACTIVE)
                .accountId(1002L)
                .build());

        this.save(Card.builder()
                .cardNumber("3456-7890-1234-5678")
                .cardHolderName("PETR SIDOROV")
                .expirationDate("2027-03-15")
                .status(CardStatus.BLOCKED)
                .accountId(1003L)
                .build());

        this.save(Card.builder()
                .cardNumber("4567-8901-2345-6789")
                .cardHolderName("ANNA VOLKOVA")
                .expirationDate("2024-12-01")
                .status(CardStatus.ACTIVE)
                .accountId(1004L)
                .build());

        this.save(Card.builder()
                .cardNumber("5678-9012-3456-7890")
                .cardHolderName("ALEXEY MOROZOV")
                .expirationDate("2028-01-31")
                .status(CardStatus.ACTIVE)
                .accountId(1005L)
                .build());
    }
}
