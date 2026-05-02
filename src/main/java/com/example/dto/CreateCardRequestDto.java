package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCardRequestDto {

    @NotBlank
    private String holderName;

    @NotBlank
    @Size(min = 32, max = 32)
    private String iban;
}
