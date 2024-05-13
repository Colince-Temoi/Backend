package tech.csm.dao;

import java.util.List;

import tech.csm.domain.Bill;
import tech.csm.domain.BillProduct;
import tech.csm.domain.Product;

public interface ProductDao {

	List<Product> getAllProducts();

	Product getProductById(Integer pId);

	

}
