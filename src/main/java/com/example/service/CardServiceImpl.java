package com.example.service;

import java.util.List;
import java.time.YearMonth;

import com.example.repository.CardRepository;
import com.example.utils.CardNumberGenerator;
import org.apache.commons.lang3.StringUtils;

import com.example.model.Card;
import com.example.model.CardStatus;
import com.example.exception.EntityNotFoundException;

public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card create(Card card) {
        validateBeforeCreation(card);

        card.setExpirationDate(YearMonth.now().plusYears(3));
        card.setStatus(CardStatus.ACTIVE);
        card.setNumber(CardNumberGenerator.generateRandomNumber());

        return cardRepository.insert(card);
    }

    @Override
    public Card getById(Long id) {
        return cardRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with id: %d".formatted(id)));
    }

    @Override
    public List<Card> getAll() {
        return cardRepository.getAll();
    }

    @Override
    public void deleteById(Long id) {
        cardRepository.deleteById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with id: %d".formatted(id)));
    }

    @Override
    public Card changeStatus(Long id, CardStatus status) {
        Card card = new Card();
        card.setStatus(status);
        return cardRepository.update(id, card)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with id: %d".formatted(id)));
    }

    private void validateBeforeCreation(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card is required");
        }
        checkHolderNameCorrectness(card.getHolderName());
        checkAccountIdCorrectness(card.getAccountId());
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
        boolean accountExists = true;        // TODO: проверка на наличие счёта
        if (!accountExists) {
            throw new IllegalArgumentException("Account not found with id: %d".formatted(accountId));
        }
    }
}
