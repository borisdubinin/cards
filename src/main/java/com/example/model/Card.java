package com.example.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card extends CardData {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
