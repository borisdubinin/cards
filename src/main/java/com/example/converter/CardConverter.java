package com.example.converter;

import com.example.dto.CardRequestDto;
import com.example.dto.CardResponseDto;
import com.example.dto.CardStatusUpdateRequestDto;
import com.example.model.Card;
import com.example.model.CardStatus;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CardConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Card toModel(CardRequestDto requestDto) {
        return Card.builder()
                .holderName(requestDto.getHolderName())
                .accountId(requestDto.getAccountId())
                .build();
    }

    public CardStatus toModel(CardStatusUpdateRequestDto requestDto) {
        return CardStatus.valueOf(requestDto.status());
    }

    public CardResponseDto toDto(Card card) {
        return CardResponseDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .holderName(card.getHolderName())
                .expirationDate(card.getExpirationDate().toString())
                .accountId(card.getAccountId())
                .createdAt(card.getCreatedAt().format(FORMATTER))
                .updatedAt(card.getUpdatedAt().format(FORMATTER))
                .build();
    }

    public List<CardResponseDto> toDto(List<Card> cards) {
        return cards.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
