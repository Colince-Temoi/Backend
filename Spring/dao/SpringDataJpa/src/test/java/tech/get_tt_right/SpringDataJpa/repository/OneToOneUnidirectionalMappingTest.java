package tech.get_tt_right.SpringDataJpa.repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.get_tt_right.SpringDataJpa.entity.Address;
import tech.get_tt_right.SpringDataJpa.entity.Order;

import java.math.BigDecimal;

@SpringBootTest
public class OneToOneUnidirectionalMappingTest {

    @Autowired
    private OrderRepository orderRepository;

//    Save Order method test case
    @Test
    void saveOrderMethod(){
        Order order = new Order();
        order.setOrderTrackingNumber("1000ABC");
        order.setTotalQuantity(5);
        order.setStatus("IN PROGRESS");
        order.setTotalPrice(new BigDecimal(1000));

        Address address = new Address();
        address.setCity("Pune");
        address.setStreet("Kothrud");
        address.setState("Maharashtra");
        address.setCountry("India");
        address.setZipCode("411047");

        order.setBillingAddress(address);

        orderRepository.save(order);

    }

    @Test
    void getOrderMethod(){
        Order order = orderRepository.findById(2L).get();
        System.out.println(order.toString());
    }
//   Update Order method test case
    @Test
    void updateOrderMethod(){
        Order order = orderRepository.findById(1L).get();
        order.setStatus("DELIVERED");
        order.getBillingAddress().setZipCode("411087");
        orderRepository.save(order);
    }
//    Delete Order method test case
    @Test
    void deleteOrderMethod(){
        orderRepository.deleteById(1L);
    }
}
