package tech.csm.productcrud.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import tech.csm.productcrud.dao.ProductDao;
import tech.csm.productcrud.domain.ProductDto;
import tech.csm.productcrud.domain.ProductVo;

public class ProductServiceImpl implements ProductService {
    private ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public String addProduct(ProductVo productVo) {
        ProductDto productDto = new ProductDto();
        productDto.setProductName(productVo.getProductName());
        productDto.setPrice(Double.parseDouble(productVo.getPrice()));
        productDto.setManufacturingDate(LocalDate.parse(productVo.getManufacturingDate(), DateTimeFormatter.ISO_DATE));

        return productDao.addProduct(productDto);
    }
    
    @Override
    public void showAllProducts() {
        ArrayList<ProductDto> products = productDao.getAllProducts();
        for (ProductDto productDto : products) {
            System.out.println("Product Id: " + products.indexOf(productDto));
            System.out.println("Product Name: " + productDto.getProductName());
            System.out.println("Product Price: " + productDto.getPrice());
            System.out.println("Product Manufacturing Date: " + productDto.getManufacturingDate());
            System.out.println();
        }
    }

    @Override
    public ProductVo searchProductById(String id) {
        ProductDto productDto = productDao.getProductById(id);
        if (productDto != null) {
            ProductVo productVo = new ProductVo();
            productVo.setProductName(productDto.getProductName());
            productVo.setPrice(String.valueOf(productDto.getPrice()));
            productVo.setManufacturingDate(productDto.getManufacturingDate().toString());
            return productVo;
        } else {
            return null;
        }
    }

    @Override
    public void updateProductById(String id, ProductVo productVo) {
        ProductDto productDto = new ProductDto();
        productDto.setProductName(productVo.getProductName());
        productDto.setPrice(Double.parseDouble(productVo.getPrice()));
        productDto.setManufacturingDate(LocalDate.parse(productVo.getManufacturingDate(), DateTimeFormatter.ISO_DATE));
        productDao.updateProductById(id, productDto);
    }

    @Override
    public void deleteProductById(String id) {
        productDao.deleteProductById(id);
    }
}