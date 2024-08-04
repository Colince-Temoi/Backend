package tech.get_tt_right.SpringDataJpa.repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.get_tt_right.SpringDataJpa.entity.Address;
import tech.get_tt_right.SpringDataJpa.entity.Order;

import java.math.BigDecimal;

@SpringBootTest
public class OneToOneBidirectionalMappingTest {

    @Autowired
    private AddressRepository addressRepository;
//    Save Address with associated Order method test case
    @Test
    void saveAddressMethod(){

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
        address.setOrder(order);

        addressRepository.save(address);
    }
//    Update Address with associated Order method test case
    @Test
    void updateAddressMethod(){
        Address address = addressRepository.findById(1L).get();
        address.setZipCode("411048");

        address.getOrder().setStatus("DELIVERED");

        addressRepository.save(address);
    }
//    Fetch Address with associated Order method test case
    @Test
    void fetchAddressMethod(){
        Address address = addressRepository.findById(2L).get();
//        System.out.println(address.toString());
    }
//   Delete Address with associated Order method test case
    @Test
    void deleteAddressMethod(){
        addressRepository.deleteById(1L);
    }
}
