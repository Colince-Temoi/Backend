package com.get_tt_right.profile.service.impl;

import com.get_tt_right.common.event.AccountDataChangedEvent;
import com.get_tt_right.common.event.CardDataChangedEvent;
import com.get_tt_right.common.event.CustomerDataChangedEvent;
import com.get_tt_right.common.event.LoanDataChangedEvent;
import com.get_tt_right.profile.constants.ProfileConstants;
import com.get_tt_right.profile.dto.ProfileDto;
import com.get_tt_right.profile.entity.Profile;
import com.get_tt_right.profile.exception.ResourceNotFoundException;
import com.get_tt_right.profile.mapper.ProfileMapper;
import com.get_tt_right.profile.repository.ProfileRepository;
import com.get_tt_right.profile.service.IProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements IProfileService {

    private ProfileRepository profileRepository;

    /** 1st using the mobile number that I have received, I need to identify whether there  is an existing record inside the profile MV/DB. That's why using the profileRepository, I am going to invoke the findByMobileNumberAndActiveSw method. To this I am going to pass the mobile number that I have received from the CustomerDataChangedEvent, followed by the 2nd parameter I am going to mention as true or alternatively I can also mention the constant from the ProfileConstants class i.e., ProfileConstants.ACTIVE_SW.
     * In case if there is no record inside my DB with a given mobile number, we want to create a new Profile object, that's why we are trying to invoke the orElseGet method and from this method I am going to return the new object of Profile - So let is use constructor reference here i.e., Profile::new. then semicolon and on the LHS of the equals signe I am going to catch the output with the variable name as profile itself. So what that line of code is going to do? It is going to check the DB if there is an existing record. If yes! it is going to assign the existing record object to the Profile object variable otherwise we are going to get a brand-new object of Profile and assign it to the Profile object variable.
     * Next, into this profile object I can try to set the mobile number by reading it from the CustomerDataChangedEvent object. Very similarly, I can try to set the name as well, after the name I can try to set the active switch as well. Though as the docstring is saying and looks good, there is some special scenario that we need to consider here, as of now, you can see we are trying to blindly set the name from the event object to the profile object, but in the scenario of delete operation - inside the CustomerServiceImpl, if you can see the logic there we are trying to send only the mobile number and active switch but not the name and with that this CustomerDataChanged object is going to have the name value as "null" and if you try to set the same null value here, the saving of the profile object is going to fail because name is a non-null column inside the profile DB. To overcome this challange, what we can try to do is - we can have an if null condition/statement and using the CustomerDataChangedEvent object I am going to check if the name is not null. If it is not null then ONLY I am going to set/populate the name value as well into the profile object, otherwise I am not going to make any change to the existing name inside the DB.
     * Towards the end, using profileRepository, I am going to save the profile object. With what we have written here, you should now be crisp clear.
     * */
    @Override
    public void handleCustomerDataChangedEvent(CustomerDataChangedEvent customerDataChangedEvent) {
        Profile profile =profileRepository.findByMobileNumberAndActiveSw(customerDataChangedEvent.getMobileNumber(),
                ProfileConstants.ACTIVE_SW).orElseGet(Profile::new);
        profile.setMobileNumber(customerDataChangedEvent.getMobileNumber());
        if (customerDataChangedEvent.getName() != null) {
            profile.setName(customerDataChangedEvent.getName());
        }
        profile.setActiveSw(customerDataChangedEvent.isActiveSw());
        profileRepository.save(profile);
    }

    /**In the 1st line of code - its is going to check is a profile record is present inside the MV or DB. If it is not present I want to throw a ResourceNotFoundException exception because by the time this AccountDataChanged event is being processed, I am expecting that the customer data is already present inside the profile MV/DB. And that's how the UI application is also going to work.
     * 1st a customer will be created followed by the customer can create an acct, loan or card. That's why in the case where a profile is not available with the given mobile number - in this scenario we want to throw an exception by using orElseThrow and the exception that we want to throw is a ResourceNotFoundException of type resource Profile with the given mobile number. Whereas if there is an existing record, I am going to catch the record with the variable name as profile.
     * Using the same profile variable/object, I am going to invoke the method which is setAccountNumber, and I am going to get the account number from the AccountDataChangedEvent. And towards the end we can try to invoke the save method on the profileRepository by passing the profile object.
     * With this you should be crisp clear - same drill/same logic as discussed in this handleAccountDataChangedEvent method docstring we will have it inside the handleLoanDataChangedEvent and handleCardDataChangedEvent methods. Nothing new!!
     * */
    @Override
    public void handleAccountDataChangedEvent(AccountDataChangedEvent accountDataChangedEvent) {
        Profile profile = profileRepository.findByMobileNumberAndActiveSw(accountDataChangedEvent.getMobileNumber(),
                        ProfileConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "mobileNumber", accountDataChangedEvent.getMobileNumber()));
        profile.setAccountNumber(accountDataChangedEvent.getAccountNumber());
        profileRepository.save(profile);
    }

    @Override
    public void handleLoanDataChangedEvent(LoanDataChangedEvent loanDataChangedEvent) {
        Profile profile = profileRepository.findByMobileNumberAndActiveSw(loanDataChangedEvent.getMobileNumber(),
                        ProfileConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "mobileNumber", loanDataChangedEvent.getMobileNumber()));
        profile.setLoanNumber(loanDataChangedEvent.getLoanNumber());
        profileRepository.save(profile);
    }

    @Override
    public void handleCardDataChangedEvent(CardDataChangedEvent customerDataChangedEvent) {
        Profile profile = profileRepository.findByMobileNumberAndActiveSw(customerDataChangedEvent.getMobileNumber(),
                        ProfileConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "mobileNumber", customerDataChangedEvent.getMobileNumber()));
        profile.setCardNumber(customerDataChangedEvent.getCardNumber());
        profileRepository.save(profile);
    }

    @Override
    public ProfileDto fetchProfile(String mobileNumber) {
        Profile profile = profileRepository.findByMobileNumberAndActiveSw(mobileNumber, true).orElseThrow(
                () -> new ResourceNotFoundException("Profile", "mobileNumber", mobileNumber)
        );
        ProfileDto profileDto = ProfileMapper.mapToProfileDto(profile, new ProfileDto());
        return profileDto;
    }
}
