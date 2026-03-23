package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class CardResponseDto {

    private Long id;
    private String number;

    @JsonProperty("holder_name")
    private String holderName;

    @JsonProperty("expiration_date")
    private String expirationDate;

    private String status;

    @JsonProperty("account_id")
    private Long accountId;
}
