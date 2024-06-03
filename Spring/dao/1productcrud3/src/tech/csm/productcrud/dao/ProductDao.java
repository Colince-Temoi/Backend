package tech.csm.productcrud.dao;

import java.util.ArrayList;

import tech.csm.productcrud.domain.ProductDto;

public interface ProductDao {
	String addProduct(ProductDto productDto);
    ArrayList<ProductDto> getAllProducts();
    ProductDto getProductById(String id);
    void updateProductById(String id, ProductDto productDto);
    void deleteProductById(String id);
    // Add your method signatures here
}