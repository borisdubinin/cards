package com.example.service;

import java.util.List;

import com.example.model.Card;
import com.example.model.CardStatus;

public interface CardService {

    /**
     * @param card card with required fields accountId and holderName
     * @return created card
     */
    Card create(Card card);

    /**
     * @param id id of the card you need to get
     */
    Card getById(Long id);

    /**
     * @return returns all cards
     */
    List<Card> getAll();

    /**
     * @param id id of the card you need to update
     * @param card card with required field accountId and/or holderName
     * @return returns updated card
     */
    Card update(Long id, Card card);

    /**
     * @param id id of the card you need to delete
     */
    void delete(Long id);

    /**
     * @param id id of the card you need to edit
     * @param status status you want to set
     * @return returns edited card
     */
    Card changeStatus(Long id, CardStatus status);
}
