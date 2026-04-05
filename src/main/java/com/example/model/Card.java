package com.example.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class Card extends CardData {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
