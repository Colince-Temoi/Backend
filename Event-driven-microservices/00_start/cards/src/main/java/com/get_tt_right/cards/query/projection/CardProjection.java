package com.get_tt_right.cards.query.projection;

import com.get_tt_right.cards.command.event.CardCreatedEvent;
import com.get_tt_right.cards.command.event.CardDeletedEvent;
import com.get_tt_right.cards.command.event.CardUpdatedEvent;
import com.get_tt_right.cards.entity.Cards;
import com.get_tt_right.cards.service.ICardsService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("card-group")
public class CardProjection {
    private final ICardsService iCardsService;

    @EventHandler
    public void on(CardCreatedEvent event) {
        Cards cardEntity = new Cards();
        BeanUtils.copyProperties(event, cardEntity);
        iCardsService.createCard(cardEntity);
    }

    @EventHandler
    public void on(CardUpdatedEvent event) {
        iCardsService.updateCard(event);
    }

    @EventHandler
    public void on(CardDeletedEvent event) {
        iCardsService.deleteCard(event.getCardNumber());
    }
}
