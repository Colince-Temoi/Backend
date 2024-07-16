package tech.get_tt_right.SpringDataJpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.get_tt_right.SpringDataJpa.entity.Product;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void saveMethodTest() {
//        Create Product Object
        Product product = new Product();
        product.setSku("y2wx");
        product.setName("Product 1");
        product.setDescription("Product 1 description");
        product.setPrice(new BigDecimal(700.00));
        product.setActive(true);
        product.setImageUrl("https://www.google.com");
//        Save Product Object to Database
        Product savedProduct = productRepository.save(product);

//        Display the saved product
        System.out.println(savedProduct.getId());
        System.out.println(savedProduct.toString());

    }

//    Update Method Test
    @Test
    void updateMethodTest() {
//        find the product
        Product product = productRepository.findById(1L).get();
//        update the product
        product.setPrice(new BigDecimal(400.00)); // update the price of the product to 400.00
        product.setSku("XYZ123"); // update the sku of the product to XYZ123
        product.setDescription("Samsung 32 inch TV with 4k display"); // update the description of the product
        Product updatedProduct = productRepository.save(product);

//        Display the updated product
        System.out.println(updatedProduct.getId());
        System.out.println(updatedProduct.toString());
    }

//    find by id method test
    @Test
    void findByIdMethodTest() {
        Long id = 1L;
        Product product = productRepository.findById(id).get();
        System.out.println(product.getId());
        System.out.println(product.toString());
    }

//    save all method test
    @Test
    void saveAllMethodTest() {
//        Create Product Object

        Product product2 = new Product();
        product2.setSku("DEF123");
        product2.setName("Mobile");
        product2.setDescription("Samsung Galaxy S21");
        product2.setPrice(new BigDecimal(800.00));
        product2.setActive(true);
        product2.setImageUrl("https://www.google.com");

        Product product3 = new Product();
        product3.setSku("GHI123");
        product3.setName("Laptop");
        product3.setDescription("Dell Inspiron 15");
        product3.setPrice(new BigDecimal(1000.00));
        product3.setActive(true);
        product3.setImageUrl("https://www.google.com");

//        Save Product Object to Database
        productRepository.saveAll(java.util.List.of(product2, product3));

    }

//    find all method test
    @Test
    void findAllMethodTest() {
        java.util.List<Product> products = productRepository.findAll();
        products.forEach(product -> {
            System.out.println(product.getId());
            System.out.println(product.toString());
        });
    }

//    delete by id method test
    @Test
    void deleteByIdMethodTest() {
        Long id = 1L;
        productRepository.deleteById(id);
    }

//    delete method test
    @Test
    void deleteMethodTest() {
        Product product = productRepository.findById(2L).get();
        productRepository.delete(product);
    }

//    delete all method test
    @Test
    void deleteAllMethodTest() {
        productRepository.deleteAll();
    }

//    delete all iterable method test
    @Test
    void deleteAllIterableMethodTest() {
//        find by id 7 and 8
        Product product1 = productRepository.findById(7L).get();
        Product product2 = productRepository.findById(8L).get();
//        delete all by iterable
        productRepository.deleteAll(java.util.List.of(product1, product2));
    }

//    count method test
    @Test
    void countMethodTest() {
        long count = productRepository.count();
        System.out.println(count);
    }

//    exists by id method test
    @Test
    void existsByIdMethodTest() {
        Long id = 1L;
        boolean exists = productRepository.existsById(id);
        System.out.println(exists);
    }
}