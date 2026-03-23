package com.example.service;

import java.util.List;
import java.time.YearMonth;

import org.apache.commons.lang3.StringUtils;

import com.example.model.Card;
import com.example.model.CardStatus;
import com.example.exception.EntityNotFoundException;
import com.example.repository.CardRepository;

public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
        initStorage();
    }

    @Override
    public Card create(Card card) {
        validateBeforeCreation(card);

        card.setExpirationDate(YearMonth.now().plusYears(3));
        card.setStatus(CardStatus.NEW);
        card.setNumber(CardNumberGenerator.generateUniqueNumber());

        cardRepository.save(card);
        return card;
    }

    @Override
    public Card getById(Long id) {
        return cardRepository.getById(id).orElseThrow(
                () -> new EntityNotFoundException("Card not found with id: " + id));
    }

    @Override
    public List<Card> getAll() {
        return cardRepository.getAll();
    }

    @Override
    public Card update(Long id, Card card) {
        validateBeforeUpdate(card);

        Card existingCard = getById(id);

        if (card.getHolderName() != null) {
            existingCard.setHolderName(card.getHolderName());
        }
        if (card.getAccountId() != null) {
            existingCard.setAccountId(card.getAccountId());
        }

        existingCard.setExpirationDate(YearMonth.now().plusYears(3));
        existingCard.setNumber(CardNumberGenerator.generateUniqueNumber());

        cardRepository.save(existingCard);
        return existingCard;
    }

    @Override
    public void delete(Long id) {
        cardRepository.deleteById(id).orElseThrow(
                () -> new EntityNotFoundException("Card not found with id: " + id));
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

    private void validateBeforeCreation(Card card) {
        checkForNull(card);
        checkHolderNameCorrectness(card.getHolderName());
        checkAccountIdCorrectness(card.getAccountId());
    }

    private void validateBeforeUpdate(Card card) {
        checkForNull(card);
        if (card.getHolderName() == null && card.getAccountId() == null) {
            throw new IllegalArgumentException("Card holder name or account id is required");
        }
        if (card.getHolderName() != null) {
            checkHolderNameCorrectness(card.getHolderName());
        }
        if (card.getAccountId() != null) {
            checkAccountIdCorrectness(card.getAccountId());
        }
    }

    private void checkForNull(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card is required");
        }
    }

    private void checkHolderNameCorrectness(String holderName) {
        if (StringUtils.isBlank(holderName)) {
            throw new IllegalArgumentException("Card holder name is required");
        }
    }

    private void checkAccountIdCorrectness(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account id is required");
        }
        boolean accountExists = true;        //потом тут будет проверка на наличие счёта
        if (!accountExists) {
            throw new IllegalArgumentException("Account not found with id: " + accountId);
        }
    }
}
