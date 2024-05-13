package com.get_tt_right.dao;

import java.util.List;

import com.get_tt_right.domain.Product;

public interface ProductDao {
	List<Product> getAllProducts();

	Product getProductById(Integer id);
}
