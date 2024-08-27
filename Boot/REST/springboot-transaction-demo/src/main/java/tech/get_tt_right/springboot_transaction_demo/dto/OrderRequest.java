package tech.get_tt_right.springboot_transaction_demo.dto;

import lombok.Getter;
import lombok.Setter;
import tech.get_tt_right.springboot_transaction_demo.entity.Order;
import tech.get_tt_right.springboot_transaction_demo.entity.Payment;

@Getter
@Setter
/* This OrderRequest dto class is used to basically send data from client to server
*  It contains the order and payment details
*  In order to bind the data from the client to the server, we use this class
* */
public class OrderRequest {
    private Order order;
    private Payment payment;
}
