package com.example.services;

import java.util.List;
import java.util.Optional;

import com.example.models.Card;
import com.example.models.CardStatus;
import com.example.exceptions.CardNotFoundException;
import com.example.repository.CardRepository;

public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    public CardServiceImpl() {
        this.cardRepository = new CardRepository();
    }

    @Override
    public void createCard(Card card) {
        if (card.getCardNumber() == null || card.getCardNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }
        if (card.getCardHolderName() == null || card.getCardHolderName().trim().isEmpty()) {
            throw new IllegalArgumentException("Card holder name is required");
        }

        cardRepository.save(card);
    }

    @Override
    public Card getCardById(Long id) {
        Optional<Card> card = cardRepository.getById(id);
        if(card.isPresent()) {
            return card.get();
        } else {
            throw new CardNotFoundException("Card not found with id: " + id);
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
            throw new CardNotFoundException("Card not found with id: " + id);
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
}
