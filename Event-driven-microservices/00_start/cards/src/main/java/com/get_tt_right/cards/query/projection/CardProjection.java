package com.get_tt_right.cards.query.projection;

import com.get_tt_right.cards.command.event.CardCreatedEvent;
import com.get_tt_right.cards.command.event.CardDeletedEvent;
import com.get_tt_right.cards.command.event.CardUpdatedEvent;
import com.get_tt_right.cards.entity.Cards;
import com.get_tt_right.cards.service.ICardsService;
import com.get_tt_right.common.event.AccntMobileNumUpdatedEvent;
import com.get_tt_right.common.event.CardMobNumRollbackedEvent;
import com.get_tt_right.common.event.CardMobileNumUpdatedEvent;
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

    @EventHandler
    public void on(CardMobileNumUpdatedEvent cardMobileNumUpdatedEvent) {
        iCardsService.updateMobileNumber(cardMobileNumUpdatedEvent.getMobileNumber(), cardMobileNumUpdatedEvent.getNewMobileNumber());
    }

    /** Here we are invoking the updateMobileNumber method of the ICardsService interface, but we need to swap the mobile numbers.
     * 1st we need to pass the new mobile number as the first argument (Which is already saved into the DB) and the old mobile number as the second argument
     * With the logic that we have written in the Cards ms, haha!, the compensation txn is going to be executed. Once the compensation txn on the cards ms is completed, we need to trigger the compensation txn on the accounts ms. That's why to make it happen, we need to go to the Saga manager and we need to create a new Saga event handler method which is going to handle the event of type CardMobNumRollbackedEvent. Check out the SagaManager class for more details
     * */
    @EventHandler
    public void on(CardMobNumRollbackedEvent cardMobNumRollbackedEvent) {
        iCardsService.updateMobileNumber(cardMobNumRollbackedEvent.getNewMobileNumber(), cardMobNumRollbackedEvent.getMobileNumber());
    }
}
