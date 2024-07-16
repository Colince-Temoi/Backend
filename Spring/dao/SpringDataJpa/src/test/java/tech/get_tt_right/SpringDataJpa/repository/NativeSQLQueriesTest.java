package tech.get_tt_right.SpringDataJpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.get_tt_right.SpringDataJpa.entity.Product;

@SpringBootTest
public class NativeSQLQueriesTest {
    @Autowired
    private ProductRepository productRepository;

    //    Native SQL query - getProductByNameOrDescription test
    @Test
    void getProductByNameOrDescriptionTest() {
        String name = "TV";
        String description = "Samsung 32 inch TV";
        Product product = productRepository.getProductByNameOrDescription(name, description);
        System.out.println(product.getId());
        System.out.println(product.toString());
    }

    //    Native SQL query - getProductByNameOrDescriptionNamedParams test
    @Test
    void getProductByNameOrDescriptionNamedParamsTest() {
        String name = "TV";
        String description = "Samsung 32 inch TV";
        Product product = productRepository.getProductByNameOrDescriptionNamedParams(name, description);
        System.out.println(product.getId());
        System.out.println(product.toString());
    }
}


