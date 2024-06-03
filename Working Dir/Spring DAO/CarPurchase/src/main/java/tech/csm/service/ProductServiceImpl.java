package tech.csm.service;

import java.util.List;

import tech.csm.dao.ProductDao;
import tech.csm.dao.ProductDaoImpl;
import tech.csm.entity.Product;

public class ProductServiceImpl implements ProductService {

	private ProductDao productDao=new ProductDaoImpl();
	
	@Override
	public List<Product> getProductByCategoryId(Integer cId) {
		return productDao.getProductByCategoryId(cId);
	}

}
