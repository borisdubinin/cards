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
    public void createCard(Card card) {
        if (card.getCardNumber() == null || card.getCardNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }
        if (card.getCardHolderName() == null || card.getCardHolderName().trim().isEmpty()) {
            throw new IllegalArgumentException("Card holder name is required");
        }

        card.setExpirationDate(YearMonth.now().plusYears(3));
        card.setStatus(CardStatus.NEW);
        card.setCardNumber(CardNumberGenerator.generateUniqueNumber());

        cardRepository.save(card);
    }

    @Override
    public Card getCardById(Long id) {
        Optional<Card> card = cardRepository.getById(id);
        if (card.isPresent()) {
            return card.get();
        } else {
            throw new EntityNotFoundException("Card not found with id: " + id);
        }
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.getAll();
    }

    @Override
    public void updateCard(Long id, Card card) {
        Card existingCard = getCardById(id);

        existingCard.setCardNumber(card.getCardNumber());
        existingCard.setCardHolderName(card.getCardHolderName());
        existingCard.setExpirationDate(card.getExpirationDate());
        existingCard.setAccountId(card.getAccountId());

        cardRepository.save(existingCard);
    }

    @Override
    public void deleteCard(Long id) {
        boolean wasDeleted = cardRepository.deleteById(id).isPresent();
        if (!wasDeleted) {
            throw new EntityNotFoundException("Card not found with id: " + id);
        }
    }

    @Override
    public void blockCard(Long id) {
        Card card = getCardById(id);
        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new IllegalStateException("Card is already blocked");
        }
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Override
    public void unblockCard(Long id) {
        Card card = getCardById(id);
        if (card.getStatus() == CardStatus.ACTIVE) {
            throw new IllegalStateException("Card is already active");
        }
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
    }

    private void initStorage() {
        this.createCard(Card.builder()
                .cardHolderName("IVAN PETROV")
                .accountId(1001L)
                .build());

        this.createCard(Card.builder()
                .cardHolderName("MARIA SOKOLOVA")
                .accountId(1002L)
                .build());

        this.createCard(Card.builder()
                .cardHolderName("PETR SIDOROV")
                .accountId(1003L)
                .build());

        this.createCard(Card.builder()
                .cardHolderName("ANNA VOLKOVA")
                .accountId(1004L)
                .build());

        this.createCard(Card.builder()
                .cardHolderName("ALEXEY MOROZOV")
                .accountId(1005L)
                .build());
    }
}
