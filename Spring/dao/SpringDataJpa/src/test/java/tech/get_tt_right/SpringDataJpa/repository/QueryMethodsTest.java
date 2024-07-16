package tech.get_tt_right.SpringDataJpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.get_tt_right.SpringDataJpa.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class QueryMethodsTest {
    @Autowired
    ProductRepository productRepository;

//    query method - find by field name test
    @Test
    void findByFieldNameTest() {
        String name = "TV";
        Product product = productRepository.findByName(name);
        System.out.println(product.getId());
        System.out.println(product.toString());
    }

//    query method - find by field id test
    @Test
    void findByIdTest() {
        Long id = 11L;
        Product product = productRepository.findById(id).get();
        System.out.println(product.getId());
        System.out.println(product.toString());
    }

//    query method - find by field name or description test
    @Test
    void findByNameOrDescriptionTest() {
        String name = "TV";
        String description = "Samsung 32 inch TV";
        productRepository.findByNameOrDescription(name, description).forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

//    query method - find by field name and description test
    @Test
    void findByNameAndDescriptionTest() {
        String name = "TV";
        String description = "Samsung 32 inch TV";
        productRepository.findByNameAndDescription(name, description).forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

//    query method - find distinct by field name test
    @Test
    void findDistinctByNameTest() {
        String name = "TV";
        Product product = productRepository.findDistinctByName(name);
        System.out.println(product.getId());
        System.out.println(product.toString());
    }

//    query method - find by field price greater than test
    @Test
    void findByPriceGreaterThanTest() {
        productRepository.findByPriceGreaterThan(new BigDecimal(300.00)).forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

//    query method - find by field price less than test
    @Test
    void findByPriceLessThanTest() {
        productRepository.findByPriceLessThan(new BigDecimal(800.00)).forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

//    query method - find by field name containing test
    @Test
    void findByNameContainingTest() {
        String name = "o";
        productRepository.findByNameContaining(name).forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

//    query method - find by field name like test
    @Test
    void findByNameLikeTest() {
        String name = "%TV%";
        productRepository.findByNameLike(name).forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

//    query method - find by field price between test
    @Test
    void findByPriceBetweenTest() {
        productRepository.findByPriceBetween(new BigDecimal(300.00), new BigDecimal(800.00)).forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

    //    query method - find by date created between test
    @Test
    void findByDateCreatedBetweenTest() {
      // start date and end date
        LocalDateTime startDate = LocalDateTime.of(2024, 7, 1, 5, 52, 41);
        LocalDateTime endDate = LocalDateTime.of(2024, 7, 1, 5, 54, 48);
        productRepository.findByDateCreatedBetween(startDate, endDate).forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

    //    query method - find by name in test
    @Test
    void findByNameInTest() {
        // Use List.of to create a list of names
        productRepository.findByNameIn(List.of("TV", "Laptop")).forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

    //    query method - find first 2 by order by name in  ASC order test
    @Test
    void findFirst2ByOrderByNameAscTest() {
        productRepository.findFirst2ByOrderByNameAsc().forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

    //    query method - find first 3 by order by price in DSC order test
    @Test
    void findTop3ByOrderByPriceDescTest() {
        productRepository.findTop3ByOrderByPriceDesc().forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

}
