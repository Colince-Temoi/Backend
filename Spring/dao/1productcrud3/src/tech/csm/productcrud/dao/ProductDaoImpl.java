package tech.csm.productcrud.dao;

import java.util.ArrayList;

import tech.csm.productcrud.domain.ProductDto;

public class ProductDaoImpl implements ProductDao {
    private ArrayList<ProductDto> products;

    public ProductDaoImpl() {
        this.products = new ArrayList<>();
    }
    @Override
    public String addProduct(ProductDto productDto) {
        products.add(productDto);
        return String.valueOf(products.indexOf(productDto));
    }

    
    @Override
    public ArrayList<ProductDto> getAllProducts() {
        return products;
    }

    @Override
    public ProductDto getProductById(String id) {
        int index = Integer.parseInt(id);
        if (index >= 0 && index < products.size()) {
            return products.get(index);
        } else {
            return null;
        }
    }
    
    @Override
    public void updateProductById(String id, ProductDto productDto) {
        int index = Integer.parseInt(id);
        if (index >= 0 && index < products.size()) {
            products.set(index, productDto);
        }
    }

    @Override
    public void deleteProductById(String id) {
        int index = Integer.parseInt(id);
        if (index >= 0 && index < products.size()) {
            products.remove(index);
        }
    }
}