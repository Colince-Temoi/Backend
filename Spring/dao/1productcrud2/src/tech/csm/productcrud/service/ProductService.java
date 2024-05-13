package tech.csm.productcrud.service;

import java.util.List;

import tech.csm.productcrud.domain.ProductVo;

public interface ProductService {

	String addProduct(ProductVo productVo);

	List<ProductVo> showAllProducts();

	ProductVo searchProductById(int searchId);

	Boolean updateProductById(int updateId, ProductVo updatedProductVo);

	Boolean deleteProductById(int deleteId);

}
