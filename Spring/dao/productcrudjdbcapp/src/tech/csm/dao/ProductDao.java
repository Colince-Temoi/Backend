package tech.csm.dao;

import java.util.List;

import tech.csm.domain.Product;

public interface ProductDao {

	String saveProduct(Product convertFromVoToDto);

	Product getProductById(Integer id);

	List<Product> getAllProducts();

	String updateProduct(Product existingProduct);

	String deleteProduct(Product existingProduct);

}
