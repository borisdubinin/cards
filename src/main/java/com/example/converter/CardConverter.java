package com.example.converter;

import com.example.dto.CardRequestDto;
import com.example.dto.CardResponseDto;
import com.example.model.Card;

import java.util.List;
import java.util.stream.Collectors;

public class CardConverter {

    public Card toModel(CardRequestDto requestDto) {
        Card card = new Card();
        card.setHolderName(requestDto.getHolderName());
        card.setAccountId(requestDto.getAccountId());
        return card;
    }

    public CardResponseDto toDto(Card card) {
        return CardResponseDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .holderName(card.getHolderName())
                .expirationDate(card.getExpirationDate())
                .status(card.getStatus())
                .accountId(card.getAccountId())
                .createdAt(card.getCreatedAt())
                .updatedAt(card.getUpdatedAt())
                .build();
    }

    public List<CardResponseDto> toDtos(List<Card> cards) {
        return cards.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
