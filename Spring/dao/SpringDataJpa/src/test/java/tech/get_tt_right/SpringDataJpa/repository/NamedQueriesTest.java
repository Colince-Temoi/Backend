package tech.get_tt_right.SpringDataJpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.get_tt_right.SpringDataJpa.entity.Product;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class NamedQueriesTest {

    @Autowired
    private ProductRepository productRepository;

    //    Named JPQL query - findByPrice test
    @Test
    void findByPriceTest() {
        Product product = productRepository.findByPrice(new BigDecimal(1000.00));
        System.out.println(product.getId());
        System.out.println(product.toString());
    }

    //    Named JPQL query - Find All Order By Name Desc test
    @Test
    void findAllOrderByNameDescTest() {
        List<Product> products = productRepository.findAllOrderByNameDesc();
        products .forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });

//        find by findByPrice
        Product product = productRepository.findByPrice(new BigDecimal(1000.00));
        System.out.println(product.getId());
        System.out.println(product.toString());
    }

    // Named Native SQL query - Find Product ByDescription test
    @Test
    void namedNativeSQLQUeryTest() {
        String description = "Samsung 32 inch TV";
        Product product = productRepository.findByDescription(description);
        System.out.println(product.getId());
        System.out.println(product.toString());
    }

    // Named Native SQL queries - NamedNativeSQLQueriesTest
    @Test
    void namedNativeSQLQueriesTest() {
        // Find Product By Description
        String description = "Samsung 32 inch TV";
        Product product = productRepository.findByDescription(description);
        System.out.println(product.getId());
        System.out.println(product.toString());

        // Find all products by name asc
        List<Product> products = productRepository.findAllOrderByNameAsc();
        products.forEach(product1 -> {
            System.out.println(product1.getId());
            System.out.println(product1.toString());
        });
    }

}
