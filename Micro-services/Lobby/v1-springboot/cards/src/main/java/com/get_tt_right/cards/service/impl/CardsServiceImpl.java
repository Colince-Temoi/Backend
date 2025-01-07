package com.get_tt_right.cards.service.impl;

import com.get_tt_right.cards.constants.CardsConstants;
import com.get_tt_right.cards.dto.CardsDto;
import com.get_tt_right.cards.entity.Cards;
import com.get_tt_right.cards.exception.CardAlreadyExistsException;
import com.get_tt_right.cards.exception.ResourceNotFoundException;
import com.get_tt_right.cards.mapper.CardsMapper;
import com.get_tt_right.cards.repository.CardsRepository;
import com.get_tt_right.cards.service.ICardsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private CardsRepository cardsRepository;

    /** Logic :
     * 1. Check if the card already exists with the given mobileNumber
     * 2. If the card already exists, throw an exception
     * 3. If the card does not exist, create a new card with the given mobileNumber
     * 4. Save the new card details in the database
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> optionalCards= cardsRepository.findByMobileNumber(mobileNumber);
        if(optionalCards.isPresent()){
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber "+mobileNumber);
        }
        cardsRepository.save(createNewCard(mobileNumber));
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new card details
     */
    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }

    /** Logic :
     * 1. Check if the card exists with the given mobileNumber
     * 2. If the card exists, return the card details
     * 3. If the card does not exist, throw an exception
     *
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    /** Logic :
     * 1. Check if the card exists with the given cardNumber
     * 2. If the card exists, update the card details
     * 3. If the card does not exist, throw an exception
     * 4. Return true if to update is successful
     *
     * @param cardsDto - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateCard(CardsDto cardsDto) {
        Cards cards = cardsRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card", "CardNumber", cardsDto.getCardNumber()));
        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
        return  true;
    }

    /** Logic :
     * 1. Check if the card exists with the given mobileNumber
     * 2. If the card exists, delete the card details
     * 3. If the card does not exist, throw an exception
     * 4. Return true if to delete is successful
     *
     * @param mobileNumber - Input MobileNumber
     * @return boolean indicating if the delete of card details is successful or not
     */
    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        cardsRepository.deleteById(cards.getCardId());
        return true;
    }


}
