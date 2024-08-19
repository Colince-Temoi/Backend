package tech.get_tt_right.search_rest_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.get_tt_right.search_rest_api.entity.Product;
import tech.get_tt_right.search_rest_api.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    /* Inject the ProductService dependency into the ProductController class
     *  by adding a constructor that takes a ProductService parameter and assigns it to the productService field.
     * This is known as constructor-based dependency injection.
     * The @Autowired annotation is not required in this case because Spring automatically injects the ProductService bean into the ProductController bean based on the constructor parameter type.
     * This is a recommended approach for injecting dependencies in Spring applications because it makes the dependencies explicit and helps with testing and maintainability.
     * The ProductController class can now use the ProductService to handle product-related HTTP requests and delegate business logic to the ProductService implementation.
     *  */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /* Implement an endpoint to search for products by name or description containing the query string passed as a parameter.
    * The searchProducts method in the ProductService interface defines the contract for this functionality.
    * The ProductController class delegates the searchProducts request to the ProductService implementation to perform the search operation.
    * The searchProducts method returns a list of products that match the search query, and the ResponseEntity.ok method is used to wrap the response in an HTTP 200 OK status code.
    * The @GetMapping annotation specifies that this method should handle HTTP GET requests to the /search endpoint.
    * The @RequestParam annotation is used to bind the query parameter from the request URL to the query parameter of the searchProducts method.
    * This allows clients to pass a query string as a parameter in the URL, which is used to search for products containing that query string in their name or description.
    * In @RequestParam("query") String query, the query parameter is specified as a request parameter with the name query. This means that clients can pass the query parameter in the URL like /search?query=keyword to search for products containing the keyword in their name or description.
    * */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("query") String query){
        return ResponseEntity.ok(productService.searchProducts(query));
    }

    /* Implement an endpoint to create a new product.
    * The createProduct method in the ProductService interface defines the contract for this functionality.
    * The ProductController class delegates the createProduct request to the ProductService implementation to persist the new product in the database.
    * The createProduct method returns the newly created product, and the ResponseEntity.ok method is used to wrap the response in an HTTP 200 OK status code.
    * The @PostMapping annotation specifies that this method should handle HTTP POST requests to the /products endpoint.
    * The @RequestBody annotation is used to bind the product data from the request body to the product parameter of the createProduct method.
    * This allows clients to send a JSON payload containing the product data in the request body, which is used to create a new product in the database.
    * The Product entity is automatically deserialized from the JSON payload by Spring's message converters, making it easy to work with JSON data in Spring applications.
    * The ProductService implementation class is responsible for persisting the new product in the database using the ProductRepository.
    * The createProduct method returns the newly created product, which is then returned to the client as the response.
    * The client can use this endpoint to create new products by sending a POST request with the product data in the request body.
    * */
    @PostMapping
    public Product createProduct(@RequestBody Product product){
        return productService.createProduct(product);
    }
}

