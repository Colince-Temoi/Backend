package tech.get_tt_right.search_rest_api.service;

import tech.get_tt_right.search_rest_api.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> searchProducts(String query);

    Product createProduct(Product product);
}
