package com.get_tt_right.accounts.service.client;

import com.get_tt_right.accounts.dto.CardsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**Update as of 11/04/2025
 *As of now, my fallback business logic is to return null when the circuit is open or there is an error in the upstream server
 * */
@Component
public class CardsFallback implements CardsFeignClient{
    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String correlationId, String mobileNumber) {
        return null;
    }
}
