package com.get_tt_right.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.get_tt_right.dao.ProductDao;
import com.get_tt_right.domain.Product;
import com.get_tt_right.domain.ProductVo;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	
	@Override
	public List<ProductVo> getAllProducts() {
	    List<Product> productList = productDao.getAllProducts();
	    List<ProductVo> productVoList = new ArrayList<>();

	    for (Product product : productList) {
	        ProductVo productVo = new ProductVo();
	        productVo.setId(String.valueOf(product.getProductId()));
	        productVo.setName(product.getProductName());
	        productVo.setStock(String.valueOf(product.getStock()));
	        productVo.setUnitPrice(String.valueOf(product.getUnitPrice()));
	        productVoList.add(productVo);
	    }
		return productVoList;
	    
	}

	@Override
	public ProductVo getProductById(Integer id) {
		Product product = productDao.getProductById(id);
		ProductVo productVo = new ProductVo();
        productVo.setId(String.valueOf(product.getProductId()));
        productVo.setName(product.getProductName());
        productVo.setStock(String.valueOf(product.getStock()));
        productVo.setUnitPrice(String.valueOf(product.getUnitPrice()));
		return productVo;
	}


}
