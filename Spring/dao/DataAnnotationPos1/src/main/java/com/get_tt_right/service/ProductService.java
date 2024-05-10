package com.get_tt_right.service;

import java.util.List;

import com.get_tt_right.domain.ProductVo;

public interface ProductService {

	List<ProductVo> getAllProducts();

	ProductVo getProductById(Integer id);

}
