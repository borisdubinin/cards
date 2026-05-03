package com.example.repository;

import com.example.entity.CardEntity;
import com.example.exception.DatabaseOperationException;

import java.util.List;
import java.util.Optional;

/**
 * Repository for storing and managing card data.
 * Provides methods for saving, searching, getting all, and deleting cards.
 *
 * <p>Implementations of this interface should ensure thread-safety</p>
 *
 * @see CardEntity
 * @version 1.0
 */
public interface CardRepository {

    /**
     * Inserts card with cardData into storage
     *
     * @param cardEntity a {@link CardEntity} object with required fields filled in
     * @return inserted card with generated id and createdAt fields
     * @throws NullPointerException if card is null
     * @throws DatabaseOperationException if internal errors occurs
     */
    CardEntity insert(CardEntity cardEntity);

    /**
     * Updates a card in storage with unique identifier equal to id
     *
     * @param id   id of the card that is being updated
     * @param cardEntity a {@link CardEntity} object. Null fields are not updated
     * @return an {@link Optional} containing updated card with fields id, createdAt, updatedAt,
     * or empty Optional if card with such id doesn't exist
     * @throws NullPointerException if card is null
     * @throws DatabaseOperationException if internal errors occurs
     */
    Optional<CardEntity> update(Long id, CardEntity cardEntity);

    /**
     * Finds a card by its unique identifier.
     *
     * @param id the unique identifier of the card, must not be null
     * @return an {@link Optional} containing the found card, or an empty Optional if not found
     * @throws NullPointerException if id is null
     * @throws DatabaseOperationException if internal errors occurs
     */
    Optional<CardEntity> getById(Long id);

    /**
     * Returns a list of all cards in the repository.
     *
     * <p>If no cards exist, an empty list is returned (not null).</p>
     *
     * @return a list of all cards, may be empty but never null
     * @throws DatabaseOperationException if internal errors occurs
     */
    List<CardEntity> getAll();

    /**
     * Deletes a card by its unique identifier.
     *
     * <p>After successful deletion, an {@link Optional} with the deleted card is returned.
     * If no card with the specified id exists, an empty Optional is returned.</p>
     *
     * @param id the unique identifier of the card to delete, must not be null
     * @return an {@link Optional} containing the deleted card, or an empty Optional if not found
     * @throws NullPointerException if id is null
     * @throws DatabaseOperationException if internal errors occurs
     */
    Optional<CardEntity> deleteById(Long id);
}
