package com.get_tt_right.cards.command.event;

import lombok.Data;

@Data
public class CardDeletedEvent {
    private Long cardNumber;
    private boolean activeSw;
}
