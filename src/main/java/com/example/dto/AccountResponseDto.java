package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountResponseDto {

    private Long id;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
}

