package com.example.repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;

import com.example.model.Card;
import com.example.model.CardStatus;
import com.example.service.CardNumberGenerator;

public class CardRepository {

    private final Map<Long, Card> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public CardRepository() {
        initStorage();
    }

    public Card save(Card card) {
        if (card.getId() == null) {
            card.setId(idGenerator.getAndIncrement());
            card.setCreatedAt(LocalDateTime.now());
        } else {
            card.setUpdatedAt(LocalDateTime.now());
        }
        return storage.put(card.getId(), card);
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
                .expirationDate(YearMonth.of(2027, 12))
                .status(CardStatus.ACTIVE)
                .number(CardNumberGenerator.generateUniqueNumber())
                .holderName("IVAN PETROV")
                .accountId(1001L)
                .build());

        this.save(Card.builder()
                .expirationDate(YearMonth.of(2028, 9))
                .status(CardStatus.ACTIVE)
                .number(CardNumberGenerator.generateUniqueNumber())
                .holderName("MARIA SOKOLOVA")
                .accountId(1002L)
                .build());

        this.save(Card.builder()
                .expirationDate(YearMonth.of(2026, 5))
                .status(CardStatus.ACTIVE)
                .number(CardNumberGenerator.generateUniqueNumber())
                .holderName("PETR SIDOROV")
                .accountId(1003L)
                .build());

        this.save(Card.builder()
                .expirationDate(YearMonth.of(2026, 9))
                .status(CardStatus.ACTIVE)
                .number(CardNumberGenerator.generateUniqueNumber())
                .holderName("ANNA VOLKOVA")
                .accountId(1004L)
                .build());

        this.save(Card.builder()
                .expirationDate(YearMonth.of(2028, 2))
                .status(CardStatus.ACTIVE)
                .number(CardNumberGenerator.generateUniqueNumber())
                .holderName("ALEXEY MOROZOV")
                .accountId(1005L)
                .build());
    }
}
