package com.example.model;

import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;

@Getter
@Setter
public class CardData {

    private String number;
    private String holderName;
    private YearMonth expirationDate;
    private CardStatus status;
    private Long accountId;
}
