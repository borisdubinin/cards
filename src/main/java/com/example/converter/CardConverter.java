package com.example.converter;

import com.example.dto.CardRequestDto;
import com.example.dto.CardResponseDto;
import com.example.model.Card;
import com.example.model.CardStatus;

public class CardConverter {

    public static Card toCard(CardRequestDto requestDto) {
        return Card.builder()
                .holderName(requestDto.getHolderName())
                .status(CardStatus.valueOf(requestDto.getStatus()))
                .accountId(requestDto.getAccountId())
                .build();
    }

    public static CardResponseDto toCardResponseDto(Card card) {
        return CardResponseDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .holderName(card.getHolderName())
                .expirationDate(card.getExpirationDate().toString())
                .status(card.getStatus().toString())
                .accountId(card.getAccountId())
                .build();
    }
}
