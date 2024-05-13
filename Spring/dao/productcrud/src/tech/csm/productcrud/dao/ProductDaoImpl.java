package tech.csm.productcrud.dao;

import java.util.ArrayList;
import java.util.List;

import tech.csm.productcrud.domain.Product;

public class ProductDaoImpl implements ProductDao {

	ArrayList<Product> productList;

	public ProductDaoImpl() {
		productList = new ArrayList<>();
	}

	@Override
	public String addProduct(Product product) {
		productList.add(product);
		return "1 product added!";
	}

	@Override
	public List<Product> displayAllProducts() {
		return productList;

	}

	@Override
	public Product getProductById(Integer id) {
//		Logic to get Product by Id from the AL data store
//		1. Iterate through the AL store
//		2. Maintain some flag to indicate when the product will be found
		Product product = null;
		Boolean found = false; // initializing my flag
		for (Product x : productList) {
			if (x.getProductId().equals(id)) {
				product = x;
				found = true; // setting flag to true to indicate product is found
				break;
			}
		}
//		Like this, handle the !found case outside the loop.
		if (!found) {
			System.out.println("Product with id " + id + " does not exist. Try searching for another product!");
		}
		return product;

	}

	@Override
	public String updateProduct(Product product) {
//		find the product in store you want to update
		Product old = getProductById(product.getProductId());
		int index = productList.indexOf(old);

		productList.set(index, product);
		return "Success";
	}

	@Override
	public String deleteProduct(Integer id) {

		Product product = getProductById(id);
//		System.out.println(productList.contains(product));

		int index = productList.indexOf(product);
		productList.remove(index);

		return "Successfully removed the product with index: " + productList.indexOf(product);

	}

}
