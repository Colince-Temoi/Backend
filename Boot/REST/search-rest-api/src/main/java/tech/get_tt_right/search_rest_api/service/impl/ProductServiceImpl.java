package tech.get_tt_right.search_rest_api.service.impl;

import org.springframework.stereotype.Service;
import tech.get_tt_right.search_rest_api.entity.Product;
import tech.get_tt_right.search_rest_api.repository.ProductRepository;
import tech.get_tt_right.search_rest_api.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    /* Inject the ProductRepository dependency into the ProductService implementation class
    *  by adding a constructor that takes a ProductRepository parameter and assigns it to the productRepository field.
    * This is known as constructor-based dependency injection.
    * The @Autowired annotation is not required in this case because Spring automatically injects the ProductRepository bean into the ProductService bean based on the constructor parameter type.
    * This is a recommended approach for injecting dependencies in Spring applications because it makes the dependencies explicit and helps with testing and maintainability.
    * The ProductService implementation class can now use the ProductRepository to interact with the database and perform CRUD operations on Product entities.
    *  */
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Implement the searchProducts method to search for products by name or description containing the query string passed as a parameter.
    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProductsSQL(query);
    }

    /* Implement the createProduct method to create a new product.
    * The createProduct method delegates the creation of the new product to the ProductRepository save method, which persists the product in the database.
    * The newly created product is returned as the result of the createProduct method.
    * */
    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}

