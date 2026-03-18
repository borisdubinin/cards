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
        initTestData();
    }

    public void save(Card card) {
        if(card.getId() == null) {
            card.setId(idGenerator.getAndIncrement());
            card.setCreatedAt(LocalDateTime.now());
            card.setUpdatedAt(LocalDateTime.now());
        }
        else {
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

    private void initTestData() {
        Card card1 = new Card();
        card1.setCardNumber("1234-5678-9012-3456");
        card1.setCardHolderName("IVAN PETROV");
        card1.setExpirationDate("2026-12-31");
        card1.setStatus(CardStatus.ACTIVE);
        card1.setAccountId(1001L);
        this.save(card1);

        Card card2 = new Card();
        card2.setCardNumber("2345-6789-0123-4567");
        card2.setCardHolderName("MARIA SOKOLOVA");
        card2.setExpirationDate("2025-06-30");
        card2.setStatus(CardStatus.ACTIVE);
        card2.setAccountId(1002L);
        this.save(card2);

        Card card3 = new Card();
        card3.setCardNumber("3456-7890-1234-5678");
        card3.setCardHolderName("PETR SIDOROV");
        card3.setExpirationDate("2027-03-15");
        card3.setStatus(CardStatus.BLOCKED);
        card3.setAccountId(1003L);
        this.save(card3);

        Card card4 = new Card();
        card4.setCardNumber("4567-8901-2345-6789");
        card4.setCardHolderName("ANNA VOLKOVA");
        card4.setExpirationDate("2024-12-01"); // скоро истечет
        card4.setStatus(CardStatus.ACTIVE);
        card4.setAccountId(1004L);
        this.save(card4);

        Card card5 = new Card();
        card5.setCardNumber("5678-9012-3456-7890");
        card5.setCardHolderName("ALEXEY MOROZOV");
        card5.setExpirationDate("2028-01-31");
        card5.setStatus(CardStatus.ACTIVE);
        card5.setAccountId(1005L);
        this.save(card5);
    }
}
