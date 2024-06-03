package tech.csm.service;

import java.util.List;

import tech.csm.entity.Product;

public interface ProductService {

	List<Product> getProductByCategoryId(Integer cId);

}
