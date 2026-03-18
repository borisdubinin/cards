package com.example.models;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Card {

    private Long id;
    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;
    private CardStatus status;
    private Long accountId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Card(String cardNumber, String cardHolderName, String expirationDate, CardStatus status, Long accountId) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
        this.status = status;
        this.accountId = accountId;
    }
}
