package com.example.service;

import java.util.List;
import java.util.Optional;
import java.time.YearMonth;

import com.example.model.Card;
import com.example.model.CardStatus;
import com.example.exception.EntityNotFoundException;
import com.example.repository.CardRepository;

public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    public CardServiceImpl() {
        this.cardRepository = new CardRepository();
        initStorage();
    }

    @Override
    public Card create(Card card) {
        if (card.getNumber() == null || card.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }
        if (card.getHolderName() == null || card.getHolderName().trim().isEmpty()) {
            throw new IllegalArgumentException("Card holder name is required");
        }

        card.setExpirationDate(YearMonth.now().plusYears(3));
        card.setStatus(CardStatus.NEW);
        card.setNumber(CardNumberGenerator.generateUniqueNumber());

        cardRepository.save(card);
        return card;
    }

    @Override
    public Card getById(Long id) {
        Optional<Card> card = cardRepository.getById(id);
        if (card.isPresent()) {
            return card.get();
        } else {
            throw new EntityNotFoundException("Card not found with id: " + id);
        }
    }

    @Override
    public List<Card> getAll() {
        return cardRepository.getAll();
    }

    @Override
    public Card update(Long id, Card card) {
        Card existingCard = getById(id);

        existingCard.setHolderName(card.getHolderName());
        existingCard.setAccountId(card.getAccountId());

        existingCard.setExpirationDate(YearMonth.now().plusYears(3));
        existingCard.setNumber(CardNumberGenerator.generateUniqueNumber());

        cardRepository.save(existingCard);
        return existingCard;
    }

    @Override
    public void delete(Long id) {
        boolean wasDeleted = cardRepository.deleteById(id).isPresent();
        if (!wasDeleted) {
            throw new EntityNotFoundException("Card not found with id: " + id);
        }
    }

    @Override
    public Card changeStatus(Long id, CardStatus status) {
        Card card = getById(id);
        card.setStatus(status);
        cardRepository.save(card);
        return card;
    }

    private void initStorage() {
        this.create(Card.builder()
                .holderName("IVAN PETROV")
                .accountId(1001L)
                .build());

        this.create(Card.builder()
                .holderName("MARIA SOKOLOVA")
                .accountId(1002L)
                .build());

        this.create(Card.builder()
                .holderName("PETR SIDOROV")
                .accountId(1003L)
                .build());

        this.create(Card.builder()
                .holderName("ANNA VOLKOVA")
                .accountId(1004L)
                .build());

        this.create(Card.builder()
                .holderName("ALEXEY MOROZOV")
                .accountId(1005L)
                .build());
    }
}
