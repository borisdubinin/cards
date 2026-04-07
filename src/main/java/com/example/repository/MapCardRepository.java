package com.example.repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import com.example.model.Card;
import com.example.model.CardStatus;
import com.example.service.CardNumberGenerator;

public class MapCardRepository implements CardRepository {

    private final Map<Long, Card> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public MapCardRepository() {
        initStorage();
    }

    @Override
    public Card insert(Card card) {
        card.setId(idGenerator.getAndIncrement());
        return storage.put(card.getId(), card);
    }

    @Override
    public Optional<Card> update(Long id, Card card) {
        Card oldCard = storage.get(id);
        if (oldCard == null) return Optional.empty();
        oldCard.setNumber(Objects.requireNonNullElse(card.getNumber(), oldCard.getNumber()));
        oldCard.setHolderName(Objects.requireNonNullElse(card.getHolderName(), oldCard.getHolderName()));
        oldCard.setExpirationDate(Objects.requireNonNullElse(card.getExpirationDate(), oldCard.getExpirationDate()));
        oldCard.setStatus(Objects.requireNonNullElse(card.getStatus(), oldCard.getStatus()));
        oldCard.setAccountId(Objects.requireNonNullElse(card.getAccountId(), oldCard.getAccountId()));
        oldCard.setUpdatedAt(LocalDateTime.now());
        return Optional.ofNullable(storage.put(oldCard.getId(), oldCard));
    }

    @Override
    public Optional<Card> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Card> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Card> deleteById(Long id) {
        return Optional.ofNullable(storage.remove(id));
    }

    private void initStorage() {
        Card card1 = new Card();
        card1.setExpirationDate(YearMonth.of(2027, 12));
        card1.setStatus(CardStatus.ACTIVE);
        card1.setNumber(CardNumberGenerator.generateUniqueNumber());
        card1.setHolderName("IVAN PETROV");
        card1.setAccountId(1001L);
        insert(card1);

        Card card2 = new Card();
        card2.setExpirationDate(YearMonth.of(2028, 9));
        card2.setStatus(CardStatus.BLOCKED);
        card2.setNumber(CardNumberGenerator.generateUniqueNumber());
        card2.setHolderName("MARIA SOKOLOVA");
        card2.setAccountId(1002L);
        insert(card2);

        Card card3 = new Card();
        card3.setExpirationDate(YearMonth.of(2026, 5));
        card3.setStatus(CardStatus.ACTIVE);
        card3.setNumber(CardNumberGenerator.generateUniqueNumber());
        card3.setHolderName("PETR SIDOROV");
        card3.setAccountId(1003L);
        insert(card3);

        Card card4 = new Card();
        card4.setExpirationDate(YearMonth.of(2026, 9));
        card4.setStatus(CardStatus.BLOCKED);
        card4.setNumber(CardNumberGenerator.generateUniqueNumber());
        card4.setHolderName("ANNA VOLKOVA");
        card4.setAccountId(1004L);
        insert(card4);

        Card card5 = new Card();
        card5.setExpirationDate(YearMonth.of(2028, 2));
        card5.setStatus(CardStatus.ACTIVE);
        card5.setNumber(CardNumberGenerator.generateUniqueNumber());
        card5.setHolderName("ALEXEY MOROZOV");
        card5.setAccountId(1005L);
        insert(card5);
    }
}
