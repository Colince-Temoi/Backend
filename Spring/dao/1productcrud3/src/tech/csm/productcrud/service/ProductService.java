package tech.csm.productcrud.service;

import tech.csm.productcrud.domain.ProductVo;

public interface ProductService {

	 String addProduct(ProductVo productVo);
	    void showAllProducts();
	    ProductVo searchProductById(String id);
	    void updateProductById(String id, ProductVo productVo);
	    void deleteProductById(String id);
    // Add your method signatures here
}