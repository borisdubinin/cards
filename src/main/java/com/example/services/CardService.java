package com.example.services;

import java.util.List;

import com.example.models.Card;

public interface CardService {

    void createCard(Card card);
    Card getCardById(Long id);
    List<Card> getAllCards();
    void updateCard(Long id, Card card);
    void deleteCard(Long id);
    void blockCard(Long id);
    void unblockCard(Long id);
}
