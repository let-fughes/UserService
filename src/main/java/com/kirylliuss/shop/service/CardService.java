package com.kirylliuss.shop.service;

import com.kirylliuss.shop.dto.request.CardRequest;
import com.kirylliuss.shop.dto.respons.CardRespons;
import com.kirylliuss.shop.exceptions.CardNotFoundException;
import com.kirylliuss.shop.exceptions.ValidationException;
import com.kirylliuss.shop.mapper.CardMapper;
import com.kirylliuss.shop.model.Card;
import com.kirylliuss.shop.repository.CardRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Autowired
    public CardService(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    @Cacheable(value = "CardsByIds", key = "#user_id")
    @Transactional(readOnly = true)
    public List<CardRespons> getCardsByIdIn(List<Long> ids) {
        return cardRepository.findByIdIn(ids).stream()
                .map(cardMapper::toCardResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "cardById", key = "#id")
    @Transactional(readOnly = true)
    public CardRespons getCardById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        return cardMapper.toCardResponse(card);
    }

    @Caching(evict = {@CacheEvict(value = "createCard", allEntries = true)})
    @Transactional
    public CardRespons createCard(@Valid CardRequest cardRequest) throws ValidationException {
        Card card = cardMapper.toCard(cardRequest);
        Card savedCard = cardRepository.save(card);
        return cardMapper.toCardResponse(savedCard);
    }

    @Caching(
            evict = {
                @CacheEvict(
                        value = "userCards",
                        key = "#result.userId",
                        condition = "#result != null"),
                @CacheEvict(value = "cards", key = "#id")
            })
    @Transactional
    public CardRespons updateCard(Long id, @Valid CardRequest cardRequest) {
        Card existingCard =
                cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));

        cardMapper.updateCardFromCardRequest(existingCard, cardRequest);
        Card updatedCard = cardRepository.save(existingCard);

        return cardMapper.toCardResponse(updatedCard);
    }

    @Caching(evict = {@CacheEvict(value = "deleteCards", key = "#id")})
    @Transactional
    public void deleteCard(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException(id);
        }
        cardRepository.deleteById(id);
    }
}
