package com.get_tt_right.accounts.service.client;

import com.get_tt_right.accounts.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/** Update as of 27/3/2025
 * Here, we have modified the fetchCardsDetails signature by introducing the correlationId parameter.
 * Here, just like we have @RequestParam String mobileNumber, as copied from the CustomerController fetchAccountDetails method, we have introduced @RequestHeader("eazybank-correlation-id") String correlationId. Copy this as it is exactly in the CustomerController fetchAccountDetails method.
 *
 * I have used @Primary annotation here to solve a CE issue I was facing while trying to inject the CardsFeignClient in the CustomerServiceImpl class. i.e.,
 *    Could not autowire. There is more than one bean of 'LoansFeignClient' type.Beans:com.get_tt_right.accounts.service.client.LoansFeignClient (LoansFeignClient.java)loansFallback (LoansFallback.java)
 * Though my instructor didn't encounter this issue and didn't add any @Primary annotations as a workaround. He reacted to this issue by saying that al was well from his side and this is what he had to say,
 *  "This is strange. I am not getting any compilation error or issue. May be try deleting the content of target folder and do a maven clean build. If this is not helping, remove all the maven libraries from your local system and re download them."
 *   Though I have not tried his recommendation, in the meantime let me stick to the workround I found online - Though am no sure about its impact. If I will ever get clarity, I will update this.
 * */
@Primary
@FeignClient(name = "cards",fallback = CardsFallback.class)
public interface CardsFeignClient {
    @GetMapping(value = "/api/fetch", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestHeader("eazybank-correlation-id") String correlationId, @RequestParam String mobileNumber);
}
