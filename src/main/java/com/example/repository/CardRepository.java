package com.example.repository;

import com.example.model.Card;

import java.util.List;
import java.util.Optional;

/**
 * Repository for storing and managing card data.
 * Provides methods for saving, searching, getting all, and deleting cards.
 *
 * <p>Implementations of this interface should ensure thread-safety</p>
 *
 * @see Card
 * @version 1.0
 */
public interface CardRepository {

    /**
     * Inserts card with cardData into storage
     *
     * @param card a {@link Card} object with required fields filled in
     * @return inserted card with generated id and createdAt fields
     * @throws NullPointerException if cardData is null
     */
    Card insert(Card card);

    /**
     * Updates a card in storage with unique identifier equal to id
     *
     * @param id id of the card that is being updated
     * @param card a {@link Card} object. Null fields are not updated
     * @return updated card with fields id, createdAt, updatedAt, or null if card with such id doesn't exist
     */
    Card update(Long id, Card card);

    /**
     * Finds a card by its unique identifier.
     *
     * @param id the unique identifier of the card, must not be null
     * @return an {@link Optional} containing the found card, or an empty Optional if not found
     * @throws NullPointerException if id is null
     * @throws RuntimeException if a database access error occurs
     */
    Optional<Card> get(Long id);

    /**
     * Returns a list of all cards in the repository.
     *
     * <p>If no cards exist, an empty list is returned (not null).</p>
     *
     * @return a list of all cards, may be empty but never null
     * @throws RuntimeException if a database access error occurs
     */
    List<Card> getAll();

    /**
     * Deletes a card by its unique identifier.
     *
     * <p>After successful deletion, an {@link Optional} with the deleted card is returned.
     * If no card with the specified ID exists, an empty Optional is returned.</p>
     *
     * @param id the unique identifier of the card to delete, must not be null
     * @return an {@link Optional} containing the deleted card, or an empty Optional if not found
     * @throws NullPointerException if id is null
     * @throws RuntimeException if a database access error occurs
     */
    Optional<Card> delete(Long id);
}
