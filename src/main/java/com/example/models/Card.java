package com.example.models;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class Card {

    private Long id;
    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;
    private CardStatus status;
    private Long accountId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
