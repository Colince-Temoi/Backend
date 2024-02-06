package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.entity.Product;
import tech.csm.repository.ProductRepo;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo productRepo;
	
	@Override
	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	@Override
	public Product getProduct(Integer productId) {
//		Returning the Product by Id
		return productRepo.findById(productId).get();
	}

	@Override
	public Product updateProductStockUnits(Integer noOfUnits, Integer productId) {
		Product product = getProduct(productId);
		
		Integer updatedStockUnits = Math.max(0, product.getProductStock()-noOfUnits);
		
		product.setProductStock(updatedStockUnits);
		
		productRepo.save(product);
		
		return product;
	}
	
	

}
