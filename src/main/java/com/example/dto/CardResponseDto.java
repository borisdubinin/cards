package com.example.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CardResponseDto {

    private Long id;
    private String number;
    private String holderName;
    private String expirationDate;
    private String status;
    private Long accountId;
    private String createdAt;
    private String updatedAt;
}
