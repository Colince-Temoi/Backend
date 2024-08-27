package tech.get_tt_right.springboot_transaction_demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/* This OrderResponse dto class is used to basically send data from server to client
* It contains the order tracking number, status and message
* In order to bind the data from the server to the client, we use this class
* */
public class OrderResponse {
    private String orderTackingNumber;
    private String status;
    private String message;
}
