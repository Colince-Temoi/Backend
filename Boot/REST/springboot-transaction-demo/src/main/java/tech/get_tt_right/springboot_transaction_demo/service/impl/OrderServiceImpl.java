package tech.get_tt_right.springboot_transaction_demo.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tech.get_tt_right.springboot_transaction_demo.dto.OrderRequest;
import tech.get_tt_right.springboot_transaction_demo.dto.OrderResponse;
import tech.get_tt_right.springboot_transaction_demo.entity.Order;
import tech.get_tt_right.springboot_transaction_demo.entity.Payment;
import tech.get_tt_right.springboot_transaction_demo.exception.PaymentException;
import tech.get_tt_right.springboot_transaction_demo.repository.OrderRepository;
import tech.get_tt_right.springboot_transaction_demo.repository.PaymentRepository;
import tech.get_tt_right.springboot_transaction_demo.service.OrderService;

import java.util.UUID;
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
/* Generally we use @Autowired annotation to inject the dependencies but in this case we this class has only one parameterized constructor
*  So, Spring will automatically inject the dependencies and we don't need to use @Autowired annotation on top of the constructor
* Spring will automatically inject the dependencies whenever it finds a single parameterized constructor for a class
* */
    public OrderServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }
    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {

        /* Setting the order status to INPROGRESS and generating a random order tracking number
        *  and saving the order in the database
        * */
        Order order = orderRequest.getOrder();
        order.setStatus("INPROGRESS");
        order.setOrderTackingNumber(UUID.randomUUID().toString());
        orderRepository.save(order);

        Payment payment = orderRequest.getPayment();

        /* Making sure that the payment card type is DEBIT
        *  If the payment card type is not DEBIT then we will throw an exception
        * */
        if(!payment.getType().equals("DEBIT")){
            throw new PaymentException("Payment card type do not support");
        }

        /* Setting the order id in the payment object and saving the payment in the database
        * */
        payment.setOrderId(order.getId());
        paymentRepository.save(payment);

        /* Setting the order status plus tracking number and returning the order tracking number and status
        * in the OrderResponse object
        * No need to return a saved order or payment object after saving it in the database
        * The whole data we have saved in the database is already present in the order and payment object
        * So, we can just return the order tracking number and status in the OrderResponse object as this method is transactional
        * and if any exception occurs then the whole transaction will be rolled back
        * */
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderTackingNumber(order.getOrderTackingNumber());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setMessage("SUCCESS");
        return orderResponse;
    }
}
