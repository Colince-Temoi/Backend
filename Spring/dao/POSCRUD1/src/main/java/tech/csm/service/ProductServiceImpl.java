package tech.csm.service;

import java.util.ArrayList;
import java.util.List;

import tech.csm.dao.ProductDaoImpl;
import tech.csm.dao.Productdao;
import tech.csm.domain.Product;
import tech.csm.domain.ProductVo;

public class ProductServiceImpl implements ProductService {

	private Productdao productdao;

	public ProductServiceImpl() {
		productdao = new ProductDaoImpl();
	}

	@Override
	public List<ProductVo> getAllProducts() {
		// get all product dtos
		List<Product> products = productdao.getAllProducts();

		List<ProductVo> productVos = null;

		if (products != null) {
//			store to store vo types
			productVos = new ArrayList<>();

//			Iterate over the Dto types converting them to Vos
			for (Product product : products) {
				ProductVo productVo = new ProductVo();

				productVo.setId(product.getId().toString());
				productVo.setName(product.getName());
				productVo.setStock(product.getStock().toString());
				productVo.setUnitPrice(product.getUnitPrice().toString());

				productVos.add(productVo);
			}
		}

		return productVos;
	}

	@Override
	public ProductVo getProductById(Integer i) {
//	Invoke dao method
		Product product = productdao.getProductById(i);

		ProductVo productVo = null;
		if (product != null) {
//			store to store vo types
			productVo = new ProductVo();

// Convert Dto to Vo
			productVo.setId(product.getId().toString());
			productVo.setName(product.getName());
			productVo.setStock(product.getStock().toString());
			productVo.setUnitPrice(product.getUnitPrice().toString());
		}

		return productVo;
	}

}
