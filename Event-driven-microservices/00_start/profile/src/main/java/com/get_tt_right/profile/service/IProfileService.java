package com.get_tt_right.profile.service;

import com.get_tt_right.common.event.AccountDataChangedEvent;
import com.get_tt_right.common.event.CardDataChangedEvent;
import com.get_tt_right.common.event.CustomerDataChangedEvent;
import com.get_tt_right.common.event.LoanDataChangedEvent;
import com.get_tt_right.profile.dto.ProfileDto;

public interface IProfileService {
    /**
     * @param customerDataChangedEvent - CustomerDataChangedEvent Object
     */
    void handleCustomerDataChangedEvent(CustomerDataChangedEvent customerDataChangedEvent);
    /**
     * @param accountDataChangedEvent - AccountDataChangedEvent Object
     */
    void handleAccountDataChangedEvent(AccountDataChangedEvent accountDataChangedEvent);

    /**
     * @param loanDataChangedEvent - LoanDataChangedEvent Object
     */
    void handleLoanDataChangedEvent(LoanDataChangedEvent loanDataChangedEvent);

    /**
     * @param customerDataChangedEvent - CardDataChangedEvent Object
     */
    void handleCardDataChangedEvent(CardDataChangedEvent customerDataChangedEvent);

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Profile Details based on a given mobileNumber
     */
    /**
     * @param mobileNumber - Input Mobile Number
     * @return Profile Details based on a given mobileNumber
     */
    ProfileDto fetchProfile(String mobileNumber);

}
