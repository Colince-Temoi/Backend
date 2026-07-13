package com.get_tt_right.cards.function;

import com.get_tt_right.cards.service.ICardsService;
import com.get_tt_right.common.dto.MobileNumberUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class CardFunctions {

    @Bean
    public Consumer<MobileNumberUpdateDto> updateCardMobileNumber(ICardsService iCardsService) {
        return (mobileNumberUpdateDto) -> {
            log.info("Received  updateCardMobileNumber request  for the details: {}", mobileNumberUpdateDto);
            iCardsService.updateMobileNumber(mobileNumberUpdateDto);
        };
    }

    @Bean
    public Consumer<MobileNumberUpdateDto> rollbackCardMobileNumber(ICardsService iCardsService) {
        return (mobileNumberUpdateDto) -> {
            log.info("Received  rollbackCardMobileNumber request  for the details: {}", mobileNumberUpdateDto);
            iCardsService.rollbackMobileNumber(mobileNumberUpdateDto);
        };
    }

}