package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CardRequestDto {

    @JsonProperty("holder_name")
    private String holderName;

    private String status;

    @JsonProperty("account_id")
    private Long accountId;
}
