package tech.csm.productcrud.service;

import java.util.List;

import tech.csm.productcrud.domain.Product;
import tech.csm.productcrud.domain.ProductVo;

/* Service Layer
 * Acts as an intermediary between Controller and Dao layer
 * Functionalities
 * ---------------
 * 1. Generally,Receives data from Controller, processes it and hands the refined data to Dao layer.
 *   - The data it receives is what will act as the input to the below contracts||rules
 *   - For DRL requests, no input is passed to the rules.
 *   - For DML requests, input will be passed to the rules.
 * 2. Generally, accepts returned data||messages from the Dao.
 *   - This is what decides the return type of a rule.
 * */

public interface ProductService {

	String addProduct(ProductVo pvo);

	List<ProductVo> getAllProducts();

	ProductVo getProductById(Integer id);

	String updateProduct(ProductVo pvo);

	String deleteProduct(Integer id);

	List<Product> sortByPriceAsc();

	List<Product> sortByPriceDsc();

	List<Product> sortByManufacturingDateDesc();


}
