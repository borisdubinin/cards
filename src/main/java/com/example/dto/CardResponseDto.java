package com.example.dto;

import com.example.model.CardStatus;

import lombok.Builder;
import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.YearMonth;

@Builder
@Getter
public class CardResponseDto {

    private Long id;
    private String number;
    private String holderName;

    @JsonFormat(pattern = "MM/yy")
    private YearMonth expirationDate;

    private CardStatus status;
    private String iban;
}
