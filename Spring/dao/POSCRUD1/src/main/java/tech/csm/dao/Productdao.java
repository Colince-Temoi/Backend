package tech.csm.dao;

import java.util.List;

import tech.csm.domain.Product;

public interface Productdao {

	List<Product> getAllProducts();

	Product getProductById(Integer i);

}
