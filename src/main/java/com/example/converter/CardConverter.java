package com.example.converter;

import com.example.dto.CardRequestDto;
import com.example.dto.CardResponseDto;
import com.example.model.Card;
import com.example.model.CardStatus;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CardConverter {

    public static Card toCard(CardRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        Card card = Card.builder()
                .holderName(requestDto.getHolderName())
                .accountId(requestDto.getAccountId())
                .build();
        card.setStatus(requestDto.getStatus() == null ? null
                : CardStatus.valueOf(requestDto.getStatus()));
        return card;
    }

    public static CardResponseDto toCardResponseDto(Card card) {
        if (card == null) {
            return null;
        }
        return CardResponseDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .holderName(card.getHolderName())
                .expirationDate(card.getExpirationDate().toString())
                .status(card.getStatus().toString())
                .accountId(card.getAccountId())
                .build();
    }

    public static List<CardResponseDto> toCardResponseDto(Collection<? extends Card> cards) {
        if (cards == null) {
            return List.of();
        }
        return cards.stream()
                .map(CardConverter::toCardResponseDto)
                .collect(Collectors.toList());
    }
}
