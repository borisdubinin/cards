package com.example.service;

import java.util.List;

import com.example.model.Card;
import com.example.model.CardStatus;

public interface CardService {

    /**
     * Creates a card linked to the accountId with the holder's name
     *
     * @param card card with required fields accountId and holderName
     * @return created card
     */
    Card create(Card card);

    /**
     * Finds the card in storage and returns it
     *
     * @param id id of the card you need to get
     */
    Card getById(Long id);

    /**
     * Returns all cards
     *
     * @return list of all cards
     */
    List<Card> getAll();

    /**
     * Delete a card from storage
     *
     * @param id id of the card you need to delete
     */
    void delete(Long id);

    /**
     * Changes the card status
     *
     * @param id     id of the card you need to edit
     * @param status status you want to set
     * @return edited card
     */
    Card changeStatus(Long id, CardStatus status);
}
