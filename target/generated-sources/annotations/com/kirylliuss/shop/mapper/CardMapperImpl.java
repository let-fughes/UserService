package com.kirylliuss.shop.mapper;

import com.kirylliuss.shop.dto.request.CardRequest;
import com.kirylliuss.shop.dto.respons.CardRespons;
import com.kirylliuss.shop.model.Card;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-25T11:01:20+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Arch Linux)"
)
@Component
public class CardMapperImpl implements CardMapper {

    @Override
    public Card toCard(CardRequest cardRequest) {
        if ( cardRequest == null ) {
            return null;
        }

        Card card = new Card();

        card.setNumber( cardRequest.getNumber() );
        card.setHolder( cardRequest.getHolder() );
        card.setUserId( cardRequest.getUserId() );
        card.setExpirationDate( cardRequest.getExpirationDate() );

        return card;
    }

    @Override
    public CardRespons toCardResponse(Card card) {
        if ( card == null ) {
            return null;
        }

        CardRespons cardRespons = new CardRespons();

        cardRespons.setExpirationDate( card.getExpirationDate() );
        cardRespons.setHolder( card.getHolder() );
        cardRespons.setNumber( card.getNumber() );
        cardRespons.setUserId( card.getUserId() );
        cardRespons.setId( card.getId() );

        return cardRespons;
    }

    @Override
    public void updateCardFromCardRequest(Card card, CardRequest cardRequest) {
        if ( cardRequest == null ) {
            return;
        }

        card.setNumber( cardRequest.getNumber() );
        card.setHolder( cardRequest.getHolder() );
        card.setExpirationDate( cardRequest.getExpirationDate() );
    }
}
