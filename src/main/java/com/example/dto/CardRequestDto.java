package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequestDto {

    @NotBlank
    private String holderName;

    @NotNull
    private Long accountId;
}
