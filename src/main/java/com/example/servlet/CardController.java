package com.example.servlet;

import com.example.converter.CardConverter;
import com.example.dto.CardChangeStatusRequestDto;
import com.example.dto.CardRequestDto;
import com.example.dto.CardResponseDto;
import com.example.model.Card;
import com.example.model.CardStatus;
import com.example.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardConverter cardConverter;

    @GetMapping
    public List<CardResponseDto> getAll() {
        List<Card> allCards = cardService.getAll();
        return cardConverter.toDtos(allCards);
    }

    @GetMapping("/{id}")
    public CardResponseDto getById(@PathVariable Long id) {
        Card card = cardService.getById(id);
        return cardConverter.toDto(card);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponseDto create(@Valid @RequestBody CardRequestDto cardRequestDto) {
        Card newCard = cardConverter.toModel(cardRequestDto);
        newCard = cardService.create(newCard);
        return cardConverter.toDto(newCard);
    }

    @PatchMapping("/{id}/status")
    public CardResponseDto changeStatus(@PathVariable Long id,
                                        @Valid @RequestBody CardChangeStatusRequestDto changeStatusRequestDto) {
        CardStatus newStatus = changeStatusRequestDto.status();
        Card updatedCard = cardService.changeStatus(id, newStatus);
        return cardConverter.toDto(updatedCard);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        cardService.deleteById(id);
    }
}
