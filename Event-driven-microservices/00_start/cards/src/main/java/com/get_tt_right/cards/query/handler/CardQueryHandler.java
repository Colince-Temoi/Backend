package com.get_tt_right.cards.query.handler;

import com.get_tt_right.cards.dto.CardsDto;
import com.get_tt_right.cards.query.FindCardQuery;
import com.get_tt_right.cards.service.ICardsService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardQueryHandler {
    private final ICardsService iCardsService;

    @QueryHandler
    public CardsDto findCard(FindCardQuery query) {
        CardsDto card = iCardsService.fetchCard(query.getMobileNumber());
        return card;
    }
}
