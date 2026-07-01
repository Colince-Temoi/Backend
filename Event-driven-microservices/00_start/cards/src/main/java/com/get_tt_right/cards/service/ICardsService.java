package com.get_tt_right.cards.service;

import com.get_tt_right.cards.command.event.CardUpdatedEvent;
import com.get_tt_right.cards.dto.CardsDto;
import com.get_tt_right.cards.entity.Cards;

public interface ICardsService {

    /** Old spec
     *
     * @param mobileNumber - Mobile Number of the Customer

    void createCard(String mobileNumber);
*/
    /** New Spec
     *
     * @param card - Cards Object
     */
    void createCard(Cards card);

    /**
     *
     * @param mobileNumber - Input mobile Number
     *  @return Card Details based on a given mobileNumber
     */
    CardsDto fetchCard(String mobileNumber);

    /** Old Spec
     *
     * @param cardsDto - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not

    boolean updateCard(CardsDto cardsDto);
     */
    /** New Spec
     *
     * @param event - CardUpdatedEvent Object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean updateCard(CardUpdatedEvent event);

    /**
     *
     * @param cardNumber - Input Card Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    boolean deleteCard(Long cardNumber);

}
