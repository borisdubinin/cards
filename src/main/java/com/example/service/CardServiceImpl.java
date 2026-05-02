package com.example.service;

import java.util.List;
import java.time.YearMonth;

import com.example.converter.CardConverter;
import com.example.entity.CardEntity;
import com.example.repository.CardRepository;
import com.example.utils.CardNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import com.example.model.Card;
import com.example.model.CardStatus;
import com.example.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountService accountService;
    private final CardConverter cardConverter;

    @Override
    public Card create(Card card) {
        validateBeforeCreation(card);

        card.setExpirationDate(YearMonth.now().plusYears(3));
        card.setStatus(CardStatus.ACTIVE);
        card.setNumber(CardNumberGenerator.generateRandomCardNumber());
        CardEntity cardEntity = cardConverter.toEntity(card);
        CardEntity newEntity = cardRepository.insert(cardEntity);
        return cardConverter.toModel(newEntity);
    }

    @Override
    public Card getById(Long id) {
        CardEntity cardEntity = cardRepository.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with id: %d".formatted(id)));
        return cardConverter.toModel(cardEntity);
    }

    @Override
    public List<Card> getAll() {
        List<CardEntity> allEntities = cardRepository.getAll();
        return cardConverter.toModels(allEntities);
    }

    @Override
    public void deleteById(Long id) {
        cardRepository.deleteById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with id: %d".formatted(id)));
    }

    @Override
    public Card changeStatus(Long id, CardStatus status) {
        CardEntity cardEntity = new CardEntity();
        cardEntity.setStatus(status);
        CardEntity newEntity = cardRepository.update(id, cardEntity)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with id: %d".formatted(id)));
        return cardConverter.toModel(newEntity);
    }

    private void validateBeforeCreation(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card is required");
        }
        checkHolderNameCorrectness(card.getHolderName());
        checkIbanCorrectness(card.getIban());
    }

    private void checkHolderNameCorrectness(String holderName) {
        if (StringUtils.isBlank(holderName)) {
            throw new IllegalArgumentException("Card holder name is required");
        }
    }

    private void checkIbanCorrectness(String iban) {
        boolean accountExists = accountService.exists(iban);
        if (!accountExists) {
            throw new IllegalArgumentException("Account not found with IBAN: %s".formatted(iban));
        }
    }
}
