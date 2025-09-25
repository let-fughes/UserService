package com.kirylliuss.shop.controller;

import com.kirylliuss.shop.dto.request.CardRequest;
import com.kirylliuss.shop.dto.respons.CardRespons;
import com.kirylliuss.shop.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@Tag(name = "Cards", description = "Card management API")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/home")
    @Operation(summary = "Welcome page")
    public String home() {
        return "Card API";
    }

    @GetMapping("/id/{id}")
    @Operation(
            summary = "Get card by id",
            responses = {
                @ApiResponse(responseCode = "200", description = "Card got successfully"),
                @ApiResponse(responseCode = "404", description = "Card Not found"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<CardRespons> getCardById(@PathVariable Long id) {
        CardRespons cardRespons = cardService.getCardById(id);
        return ResponseEntity.ok(cardRespons);
    }

    @GetMapping("/batch")
    @Operation(
            summary = "Get users by list of IDs",
            responses = {
                @ApiResponse(responseCode = "200", description = "Successfully got users"),
                @ApiResponse(responseCode = "404", description = "Users not found"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<CardRespons>> getCardsByIdIn(List<Long> ids) {
        List<CardRespons> cards = cardService.getCardsByIdIn(ids);
        return ResponseEntity.ok(cards);
    }

    @PostMapping()
    @Operation(
            summary = "Create new card",
            responses = {
                @ApiResponse(responseCode = "201", description = "Card created successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid input data"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<CardRespons> createUser(
            @Valid @RequestBody CardRequest cardRequest, Long userId) {
        CardRespons createdCard = cardService.createCard(cardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "Update card",
            responses = {
                @ApiResponse(responseCode = "200", description = "Card updated successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid input data"),
                @ApiResponse(responseCode = "404", description = "Card not found"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<CardRespons> updateCard(
            @PathVariable Long id, @Valid @RequestBody CardRequest cardRequest) {
        CardRespons updatedCard = cardService.updateCard(id, cardRequest);
        return ResponseEntity.ok(updatedCard);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Delete card",
            responses = {
                @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Card not found"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}
