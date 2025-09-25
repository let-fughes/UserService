package com.kirylliuss.shop.mapper;

import com.kirylliuss.shop.dto.request.CardRequest;
import com.kirylliuss.shop.dto.respons.CardRespons;
import com.kirylliuss.shop.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CardMapper {

    // CardRequest -> Card
    @Mapping(target = "id", ignore = true)
    Card toCard(CardRequest cardRequest);

    // Card -> CardResponse
    CardRespons toCardResponse(Card card);

    // Update Card From CardRequest
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updateCardFromCardRequest(@MappingTarget Card card, CardRequest cardRequest);
}
