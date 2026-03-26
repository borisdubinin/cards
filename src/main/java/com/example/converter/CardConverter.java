package com.example.converter;

import com.example.dto.CardRequestDto;
import com.example.dto.CardResponseDto;
import com.example.model.Card;
import lombok.SneakyThrows;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CardConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Card toModel(CardRequestDto requestDto) throws Exception {
        if (Objects.isNull(requestDto)) throw new Exception("CardRequestDto cannot be null");
        return Card.builder()
                .holderName(requestDto.getHolderName())
                .accountId(requestDto.getAccountId())
                .build();
    }

    public CardResponseDto toDto(Card card) throws Exception {
        if (Objects.isNull(card)) throw new Exception("Card cannot be null");
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

    @SneakyThrows
    public List<CardResponseDto> toDto(List<Card> cards) {
        return cards.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
