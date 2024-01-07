package tech.csm.service;

import java.util.List;

import tech.csm.domain.ProductVo;

public interface ProductService {

	String addProduct(ProductVo addProduct);

	ProductVo getProductById(Integer id);

	List<ProductVo> getAllProducts();

	String updateProduct(ProductVo updatedProduct);

	String deleteProductById(Integer idToDelete);

}
