package tech.csm.service;

import java.util.ArrayList;
import java.util.List;

import tech.csm.dao.BillDao;
import tech.csm.dao.BillDaoImpl;
import tech.csm.dao.BillProductDao;
import tech.csm.dao.BillProductDaoImpl;
import tech.csm.dao.ProductDao;
import tech.csm.dao.ProductDaoImpl;
import tech.csm.domain.BillProductVO;
import tech.csm.domain.BillVO;
import tech.csm.domain.Product;
import tech.csm.domain.ProductVO;

public class ProductServiceImpl implements ProductService {
	private ProductDao productDao;
	private BillDao billDao;
	private BillProductDao billProductDao;
	public ProductServiceImpl() {
		productDao=new ProductDaoImpl();
		billDao=new BillDaoImpl();
		billProductDao=new BillProductDaoImpl();
	}
	
	
	@Override
	public List<ProductVO> getAllProducts() {
		List<Product> productList=productDao.getAllProducts();
		List<ProductVO> productVoList=null;
		if(productList!=null) {
			productVoList=new ArrayList<>();
			for(Product x:productList) {
				ProductVO pvo=new ProductVO();
				pvo.setProuctId(x.getProuctId().toString());
				pvo.setName(x.getName());
				pvo.setQnty(x.getQnty().toString());
				pvo.setUnitPrice(x.getUnitPrice().toString());
				productVoList.add(pvo);
			}
		}
		return productVoList;
	}


	@Override
	public ProductVO getProductById(Integer pId) {
		Product product=productDao.getProductById(pId);
		ProductVO pvo=null;
		if(product!=null) {
				pvo=new ProductVO();
				pvo.setProuctId(product.getProuctId().toString());
				pvo.setName(product.getName());
				pvo.setQnty(product.getQnty().toString());
				pvo.setUnitPrice(product.getUnitPrice().toString());				
			}
		return pvo;
	}


	

}
