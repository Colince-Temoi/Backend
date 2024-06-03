package tech.csm.dao;

import java.util.List;

import tech.csm.entity.Product;

public interface ProductDao {

	List<Product> getProductByCategoryId(Integer cId);

}
