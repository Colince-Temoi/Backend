package tech.csm.productcrud.dao;

import java.util.ArrayList;
import java.util.List;

import tech.csm.productcrud.domain.Product;

public class ProductDaoImpl implements ProductDao {

    private List<Product> productStore = new ArrayList<>();

	
	@Override
	public String addProduct(Product product) {
		productStore.add(product);
		System.out.println(product);
		return "Product saved successfully!";
	}


	@Override
	public List<Product> getAllProducts() {
		// Assuming productStore is an ArrayList<Product> as previously mentioned
	    return new ArrayList<>(productStore); // Return a copy to avoid external modification
	}


	@Override
	public Product getProductById(int searchId) {
		for (Product product : productStore) {
	        if (product.getProductId() == searchId) {
	            return product;
	        }
	    }
	    return null; // Or throw an exception if product not found
	}


	public Boolean updateProduct(Product updatedProduct) { 
	    for (int i = 0; i < productStore.size(); i++) { 
	        if (productStore.get(i).getProductId() == updatedProduct.getProductId()) {
	            productStore.set(i, updatedProduct); 
	            return true;
	        } 
	    } 
	    return false; // Or throw an exception if the product is not found
	} 

	public Boolean deleteProduct(int productId) { 
	    return productStore.removeIf(p -> p.getProductId() == productId); 
	} 

}
