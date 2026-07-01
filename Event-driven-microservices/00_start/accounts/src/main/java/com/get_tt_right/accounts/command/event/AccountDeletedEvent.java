package com.get_tt_right.accounts.command.event;

import lombok.Data;

@Data
public class AccountDeletedEvent {
    private Long accountNumber;
    private boolean activeSw;
}
