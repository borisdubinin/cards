package com.example.converter;

import com.example.dto.CreateCardRequestDto;
import com.example.dto.CardResponseDto;
import com.example.model.Card;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CardConverter {

    public Card toModel(CreateCardRequestDto requestDto) {
        Card card = new Card();
        card.setHolderName(requestDto.getHolderName());
        card.setIban(requestDto.getIban());
        return card;
    }

    public CardResponseDto toDto(Card card) {
        return CardResponseDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .holderName(card.getHolderName())
                .expirationDate(card.getExpirationDate())
                .status(card.getStatus())
                .iban(card.getIban())
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
