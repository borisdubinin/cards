package com.example.dto;

import com.example.model.CardStatus;
import jakarta.validation.constraints.NotNull;

public record CardChangeStatusRequestDto(@NotNull CardStatus status) {
}
