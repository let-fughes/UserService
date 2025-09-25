package com.kirylliuss.shop;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.kirylliuss.shop.dto.request.CardRequest;
import com.kirylliuss.shop.dto.respons.CardRespons;
import com.kirylliuss.shop.exceptions.CardNotFoundException;
import com.kirylliuss.shop.mapper.CardMapper;
import com.kirylliuss.shop.model.Card;
import com.kirylliuss.shop.repository.CardRepository;
import com.kirylliuss.shop.service.CardService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock private CardRepository cardRepository;

    @Mock private CardMapper cardMapper;

    @InjectMocks private CardService cardService;

    private Card testCard;
    private CardRespons testCardResponse;
    private CardRequest testCardRequest;

    @BeforeEach
    void setUp() {
        testCard = new Card();
        testCard.setId(1L);
        testCard.setNumber("1234567890123456");
        testCard.setExpirationDate(LocalDate.now());
        testCard.setHolder("John Doe");

        testCardResponse = new CardRespons();
        testCardResponse.setId(1L);
        testCardResponse.setNumber("1234567890123456");
        testCardResponse.setExpirationDate(LocalDate.now());
        testCardResponse.setHolder("John Doe");

        testCardRequest = new CardRequest();
        testCardRequest.setNumber("1234567890123456");
        testCardRequest.setExpirationDate(LocalDate.now());
        testCardRequest.setHolder("John Doe");
    }

    @Test
    @DisplayName("Get card by id")
    void getCardById_WhenCardExists_ShouldReturnCardResponse() {

        Long cardId = 1L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(testCard));
        when(cardMapper.toCardResponse(testCard)).thenReturn(testCardResponse);

        CardRespons result = cardService.getCardById(cardId);

        assertNotNull(result);
        assertEquals(cardId, result.getId());
        assertEquals("1234567890123456", result.getNumber());

        verify(cardRepository, times(1)).findById(cardId);
        verify(cardMapper, times(1)).toCardResponse(testCard);
    }

    @Test
    @DisplayName("Get card by id(not found)")
    void getCardById_WhenCardNotFound_ShouldThrowCardNotFoundException() {

        Long cardId = 999L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        CardNotFoundException exception =
                assertThrows(CardNotFoundException.class, () -> cardService.getCardById(cardId));

        assertEquals("Card with id not found: " + cardId, exception.getMessage());
        verify(cardRepository, times(1)).findById(cardId);
        verify(cardMapper, never()).toCardResponse(any());
    }

    @Test
    @DisplayName("Get cards by ids list")
    void getCardsByIdIn_ShouldReturnListOfCardResponses() {

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Card card2 = new Card();
        card2.setId(2L);
        Card card3 = new Card();
        card3.setId(3L);

        List<Card> cards = Arrays.asList(testCard, card2, card3);

        CardRespons response2 = new CardRespons();
        response2.setId(2L);
        CardRespons response3 = new CardRespons();
        response3.setId(3L);

        when(cardRepository.findByIdIn(ids)).thenReturn(cards);
        when(cardMapper.toCardResponse(testCard)).thenReturn(testCardResponse);
        when(cardMapper.toCardResponse(card2)).thenReturn(response2);
        when(cardMapper.toCardResponse(card3)).thenReturn(response3);

        List<CardRespons> results = cardService.getCardsByIdIn(ids);

        assertNotNull(results);
        assertEquals(3, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals(2L, results.get(1).getId());
        assertEquals(3L, results.get(2).getId());

        verify(cardRepository, times(1)).findByIdIn(ids);
        verify(cardMapper, times(3)).toCardResponse(any(Card.class));
    }

    @Test
    @DisplayName("New Card")
    void createCard_WithValidRequest_ShouldCreateAndReturnCard() {
        Card savedCard = new Card();
        savedCard.setId(2L);
        CardRespons savedCardResponse = new CardRespons();
        savedCardResponse.setId(2L);

        when(cardMapper.toCard(testCardRequest)).thenReturn(testCard);
        when(cardRepository.save(testCard)).thenReturn(savedCard);
        when(cardMapper.toCardResponse(savedCard)).thenReturn(savedCardResponse);

        CardRespons result = cardService.createCard(testCardRequest);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());

        verify(cardMapper, times(1)).toCard(testCardRequest);
        verify(cardRepository, times(1)).save(testCard);
        verify(cardMapper, times(1)).toCardResponse(savedCard);
    }

    @Test
    @DisplayName("Update Card")
    void updateCard_WhenCardExists_ShouldUpdateAndReturnCard() {

        Long cardId = 1L;
        CardRequest updateRequest = new CardRequest();
        updateRequest.setNumber("6543210987654321");
        updateRequest.setExpirationDate(LocalDate.now());
        updateRequest.setHolder("Jane Doe");

        Card updatedCard = new Card();
        updatedCard.setId(cardId);
        CardRespons updatedCardResponse = new CardRespons();
        updatedCardResponse.setId(cardId);
        updatedCardResponse.setHolder("Jane Doe");

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(testCard)).thenReturn(updatedCard);
        when(cardMapper.toCardResponse(updatedCard)).thenReturn(updatedCardResponse);

        CardRespons result = cardService.updateCard(cardId, updateRequest);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getHolder());

        verify(cardRepository, times(1)).findById(cardId);
        verify(cardMapper, times(1)).updateCardFromCardRequest(testCard, updateRequest);
        verify(cardRepository, times(1)).save(testCard);
        verify(cardMapper, times(1)).toCardResponse(updatedCard);
    }

    @Test
    @DisplayName("Update Card (not found)")
    void updateCard_WhenCardNotFound_ShouldThrowException() {
        // Given
        Long cardId = 999L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
                CardNotFoundException.class, () -> cardService.updateCard(cardId, testCardRequest));

        verify(cardRepository, times(1)).findById(cardId);
        verify(cardMapper, never()).updateCardFromCardRequest(any(), any());
        verify(cardRepository, never()).save(any());
    }

    @Test
    @DisplayName("Delete card")
    void deleteCard_WhenCardExists_ShouldDeleteCard() {
        Long cardId = 1L;
        when(cardRepository.existsById(cardId)).thenReturn(true);

        cardService.deleteCard(cardId);

        verify(cardRepository, times(1)).existsById(cardId);
        verify(cardRepository, times(1)).deleteById(cardId);
    }

    @Test
    @DisplayName("Delete Card(Not Found)")
    void deleteCard_WhenCardNotFound_ShouldThrowException() {
        Long cardId = 999L;
        when(cardRepository.existsById(cardId)).thenReturn(false);

        assertThrows(CardNotFoundException.class, () -> cardService.deleteCard(cardId));

        verify(cardRepository, times(1)).existsById(cardId);
        verify(cardRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Empty Id list")
    void getCardsByIdIn_WithEmptyList_ShouldReturnEmptyList() {
        List<Long> emptyIds = List.of();
        when(cardRepository.findByIdIn(emptyIds)).thenReturn(List.of());

        List<CardRespons> results = cardService.getCardsByIdIn(emptyIds);

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(cardRepository, times(1)).findByIdIn(emptyIds);
        verify(cardMapper, never()).toCardResponse(any());
    }
}
