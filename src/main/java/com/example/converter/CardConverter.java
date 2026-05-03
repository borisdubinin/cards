package com.example.converter;

import com.example.dto.CreateCardRequestDto;
import com.example.dto.CardResponseDto;
import com.example.entity.CardEntity;
import com.example.model.Card;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardConverter {

    public CardEntity toEntity(Card model) {
        CardEntity entity = new CardEntity();
        entity.setNumber(model.getNumber());
        entity.setHolderName(model.getHolderName());
        entity.setExpirationDate(model.getExpirationDate());
        entity.setStatus(model.getStatus());
        entity.setIban(model.getIban());
        return entity;
    }

    public Card toModel(CardEntity entity) {
        Card model = new Card();
        model.setId(entity.getId());
        model.setHolderName(entity.getHolderName());
        model.setExpirationDate(entity.getExpirationDate());
        model.setStatus(entity.getStatus());
        model.setIban(entity.getIban());
        return model;
    }

    public Card toModel(CreateCardRequestDto requestDto) {
        Card card = new Card();
        card.setHolderName(requestDto.getHolderName());
        card.setIban(requestDto.getIban());
        return card;
    }

    public List<Card> toModels(List<CardEntity> entities) {
        return entities.stream()
                .map(this::toModel)
                .toList();
    }

    public CardResponseDto toDto(Card card) {
        return CardResponseDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .holderName(card.getHolderName())
                .expirationDate(card.getExpirationDate())
                .status(card.getStatus())
                .iban(card.getIban())
                .build();
    }

    public List<CardResponseDto> toDtos(List<Card> cards) {
        return cards.stream()
                .map(this::toDto)
                .toList();
    }
}
