package tech.csm.productcrud.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import tech.csm.productcrud.dao.ProductDao;
import tech.csm.productcrud.dao.ProductDaoImpl;
import tech.csm.productcrud.domain.Product;
import tech.csm.productcrud.domain.ProductVo;
import tech.csm.productcrud.service.ProductService;

public class ProductServiceImpl implements ProductService {

	private ProductDao productDao;

	public ProductServiceImpl() {
		productDao = new ProductDaoImpl();
	}

	@Override
	public List<ProductVo> showAllProducts() {
		List<Product> products = productDao.getAllProducts();
		return products.stream().map(this::convertDtoToVo).collect(Collectors.toList());
	}

	@Override
	public ProductVo searchProductById(int searchId) {
		Product product = productDao.getProductById(searchId);
		return product != null ? convertDtoToVo(product) : null;
	}

	public Boolean deleteProductById(int deleteId) {
		return productDao.deleteProduct(deleteId);
	}

	@Override
	public String addProduct(ProductVo productVo) {
		Product product = convertVoToDto(productVo);
		return productDao.addProduct(product);
	}

	private Product convertVoToDto(ProductVo productVo) {
		Product product = new Product();

		product.setProductId(Integer.parseInt(productVo.getProductId()));
		product.setProductName(productVo.getProductName());
		product.setUnitPrice(Double.parseDouble(productVo.getUnitPrice()));

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			product.setManufacturingDate(dateFormat.parse(productVo.getManufacturingDate()));
		} catch (ParseException e) {
			System.out.println("Error parsing date. Setting to null.");
			// Or handle the error more gracefully
		}

		return product;
	}

	private ProductVo convertDtoToVo(Product product) {
		ProductVo productVo = new ProductVo();
		// Convert Product properties to ProductVo
		productVo.setProductId(String.valueOf(product.getProductId()));
		productVo.setProductName(product.getProductName());
		productVo.setUnitPrice(String.valueOf(product.getUnitPrice()));
		productVo.setManufacturingDate(new SimpleDateFormat("yyyy-MM-dd").format(product.getManufacturingDate()));

		// and so on for other fields, converting types as necessary
		return productVo;
	}

	public Boolean updateProductById(int updateId, ProductVo updatedProductVo) {
		Product existingProduct = productDao.getProductById(updateId);
		if (existingProduct != null) {
			Product updatedProduct = convertVoToDto(updatedProductVo);
			updatedProduct.setProductId(updateId); // Ensure we're updating the correct product
			return productDao.updateProduct(updatedProduct);
		}
		return false; // Or handle appropriately when product is not found
	}

}
