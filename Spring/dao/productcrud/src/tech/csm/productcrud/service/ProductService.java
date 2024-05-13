package tech.csm.productcrud.service;

import java.util.List;

import tech.csm.productcrud.domain.ProductVo;

public interface ProductService {

	String addProduct(ProductVo pvo);

	List<ProductVo> getAllProducts();

	ProductVo getProductById(Integer id);

	String updateProduct(ProductVo pvo);

	String deleteProduct(Integer id);


}
