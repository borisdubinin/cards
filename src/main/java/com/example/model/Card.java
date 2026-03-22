package com.example.model;

import java.time.LocalDateTime;
import java.time.YearMonth;

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
    private YearMonth expirationDate;
    private CardStatus status;
    private Long accountId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
