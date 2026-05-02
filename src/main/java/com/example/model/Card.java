package com.example.model;

import java.time.LocalDateTime;
import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    private Long id;
    private String number;
    private String holderName;
    private YearMonth expirationDate;
    private CardStatus status;
    private String iban;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
