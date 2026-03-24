package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class CardResponseDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String number;

    @JsonProperty("holder_name")
    private String holderName;

    @JsonProperty("expiration_date")
    private String expirationDate;

    @JsonProperty
    private String status;

    @JsonProperty("account_id")
    private Long accountId;
}
