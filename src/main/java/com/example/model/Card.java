package com.example.model;

import java.time.LocalDateTime;
import java.time.YearMonth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {

    private Long id;
    private String number;
    private String holderName;
    private YearMonth expirationDate;
    private CardStatus status;
    private Long accountId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
