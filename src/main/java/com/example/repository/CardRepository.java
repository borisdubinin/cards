package com.example.repository;

import com.example.model.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {

    Card save(Card card);

    Optional<Card> getById(Long id);

    List<Card> getAll();

    Optional<Card> deleteById(Long id);
}
