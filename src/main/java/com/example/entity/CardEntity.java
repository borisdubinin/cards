package com.example.entity;

import com.example.model.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardEntity {

    private Long id;
    private String number;
    private String holderName;
    private YearMonth expirationDate;
    private CardStatus status;
    private String iban;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}