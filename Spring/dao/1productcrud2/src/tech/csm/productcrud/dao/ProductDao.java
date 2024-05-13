package tech.csm.productcrud.dao;

import java.util.List;

import tech.csm.productcrud.domain.Product;

public interface ProductDao {

	String addProduct(Product product);

	List<Product> getAllProducts();

	Product getProductById(int searchId);

	Boolean updateProduct(Product updatedProduct);

	Boolean deleteProduct(int deleteId);

}
