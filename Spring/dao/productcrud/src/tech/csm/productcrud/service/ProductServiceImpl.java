package tech.csm.productcrud.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tech.csm.productcrud.dao.ProductDao;
import tech.csm.productcrud.dao.ProductDaoImpl;
import tech.csm.productcrud.domain.Product;
import tech.csm.productcrud.domain.ProductVo;

public class ProductServiceImpl implements ProductService {

	private ProductDao productDao;

	public ProductServiceImpl() {
		productDao = new ProductDaoImpl();
	}

	@Override
	public String addProduct(ProductVo pvo) {
		Product product = new Product();

//		Conversion from Vo to Dto
		product.setProductId(Integer.parseInt(pvo.getProductId()));
		product.setProductName(pvo.getProductName());
		product.setUnitPrice(Double.parseDouble(pvo.getUnitPrice()));

		try {
			product.setManufacturingDate(new SimpleDateFormat("dd/MM/yyyy").parse(pvo.getManufacturingDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return productDao.addProduct(product);

	}

	@Override
	public List<ProductVo> getAllProducts() {
//		Our logic here is to convert our objects data from respective types-Dto back to String -Vo
//		We already have ProductDaoImpl object in this class to access to dto AL of Product objects
//		We need to create an AL to hold the converted Dto, Nothing but Vo type.
		List<ProductVo> pvoList = new ArrayList<>();

//		Now, Iterate over the AL of Product Dto objects that is coming from the Data layer.
		for (Product x : productDao.displayAllProducts()) {
			ProductVo pvo = new ProductVo();
			pvo.setProductId(x.getProductId().toString());
			pvo.setProductName(x.getProductName());
			pvo.setUnitPrice(x.getUnitPrice().toString());
			pvo.setManufacturingDate(new SimpleDateFormat("dd/MM/yyyy").format(x.getManufacturingDate()));

			pvoList.add(pvo);
		}

		return pvoList;

	}

	@Override
	public ProductVo getProductById(Integer id) {
//		We can access the product we are getting from our AL store here.
//		We need to convert this Product dto back to Vo type
		ProductVo pvo = new ProductVo();
		Product product = productDao.getProductById(id);

		pvo.setProductId(product.getProductId().toString());
		pvo.setProductName(product.getProductName());
		pvo.setUnitPrice(product.getUnitPrice().toString());
		pvo.setManufacturingDate(new SimpleDateFormat("dd/MM/yyy").format(product.getManufacturingDate()));

		return pvo;

	}

	@Override
	public String updateProduct(ProductVo pvo) {
		Product product = new Product();

//		Conversion from Vo to Dto
		product.setProductId(Integer.parseInt(pvo.getProductId()));
		product.setProductName(pvo.getProductName());
		product.setUnitPrice(Double.parseDouble(pvo.getUnitPrice()));

		try {
			product.setManufacturingDate(new SimpleDateFormat("dd/MM/yyyy").parse(pvo.getManufacturingDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return productDao.updateProduct(product);

	}
	
	@Override
	public String deleteProduct(Integer id) {
//		1. Get the id from the Controller
//		2. Invoke deleteProduct method
		
		String msg = productDao.deleteProduct(id);	
		
		return msg;
 
		
	}
	


}
