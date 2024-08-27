package tech.get_tt_right.springboot_transaction_demo.service;

import tech.get_tt_right.springboot_transaction_demo.dto.OrderRequest;
import tech.get_tt_right.springboot_transaction_demo.dto.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequest);
}
