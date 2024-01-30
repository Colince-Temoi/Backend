package tech.csm.service;

import java.util.List;

import tech.csm.domain.ProductVo;

public interface ProductService {

	List<ProductVo> getAllProducts();

	ProductVo getProductById(Integer i);

}
