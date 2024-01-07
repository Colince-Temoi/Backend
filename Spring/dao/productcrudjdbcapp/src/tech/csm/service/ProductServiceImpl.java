package tech.csm.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tech.csm.dao.ProductDao;
import tech.csm.dao.ProductDaoImpl;
import tech.csm.domain.Product;
import tech.csm.domain.ProductVo;

public class ProductServiceImpl implements ProductService {
	Product product;
	ProductDao productDao;

	public ProductServiceImpl() {
		product = new Product();
		productDao = new ProductDaoImpl();
	}

	@Override
	public String addProduct(ProductVo productVo) {
		return productDao.saveProduct(convertFromVoToDto(productVo));

	}

	public Product convertFromVoToDto(ProductVo pvo) {

//		Conversion from Vo to Dto

		product.setProductName(pvo.getProductName());
		product.setUnitPrice(Double.parseDouble(pvo.getUnitPrice()));

		try {
			product.setMfgDate(new SimpleDateFormat("dd/MM/yyyy").parse(pvo.getMfgDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return product;

	}

	@Override
	public ProductVo getProductById(Integer id) {
		ProductVo pvo = null;
//		 Invoke dao method
		Product product = productDao.getProductById(id);
		if (product != null) {
//			Covert product dto to vo type
			pvo = convertFromDtotoVo(product);

		}
//		Return the pvo object back to the caller.
		return pvo;
	}

	public ProductVo convertFromDtotoVo(Product product) {

//		Conversion from Dto to Vo

		ProductVo productVo = new ProductVo();

		productVo.setProductId(product.getProductId().toString());
		productVo.setProductName(product.getProductName());
		productVo.setUnitPrice(product.getUnitPrice().toString());
		productVo.setMfgDate(new SimpleDateFormat("dd/MM/yyy").format(product.getMfgDate()));

		return productVo;

	}

	@Override
	public List<ProductVo> getAllProducts() {
		List<ProductVo> pvolist = new ArrayList<>();
		List<Product> list = productDao.getAllProducts();
//		Convert each product to vo
		for (Product product : list) {
			ProductVo pvo = convertFromDtotoVo(product);
			pvolist.add(pvo);
		}
		return pvolist;
	}

	@Override
	public String updateProduct(ProductVo updatedProduct) {
		Product existingProduct = productDao.getProductById(Integer.parseInt(updatedProduct.getProductId()));

		if (existingProduct != null) {
			// Update the existing product with new information
			existingProduct.setProductName(updatedProduct.getProductName());
			existingProduct.setUnitPrice(Double.parseDouble(updatedProduct.getUnitPrice()));

			try {
				existingProduct.setMfgDate(new SimpleDateFormat("dd/MM/yyyy").parse(updatedProduct.getMfgDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// Call the dao method to update the product
			return productDao.updateProduct(existingProduct);
		} else {
			return "Product with id " + updatedProduct.getProductId()
					+ " not found! Try updating with a valid product id!";
		}
	}

	@Override
	public String deleteProductById(Integer idToDelete) {
		Product existingProduct = productDao.getProductById(idToDelete);

		if (existingProduct != null) {
			// Call the dao method to delete the product
			return productDao.deleteProduct(existingProduct);
		} else {
			return "Product with id " + idToDelete + " not found! Try deleting with a valid product id!";
		}
	}
}
