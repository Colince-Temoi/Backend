package com.get_tt_right.dao;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.get_tt_right.domain.Product;

@Repository
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Product> getAllProducts() {

		List<Product> productList = jdbcTemplate.execute("{call p_productscreen(?,?,?,?,?,?)}", (CallableStatementCallback<List<Product>>) csc -> {
            csc.setString(1, "SELECT");
            csc.setInt(2, Types.NULL);
            csc.setString(3, null);
            csc.setInt(4, Types.NULL);
            csc.setDouble(5, Types.NULL);
            csc.registerOutParameter(6, Types.VARCHAR);
            ResultSet rs = csc.executeQuery();
            List<Product> products = new ArrayList<>();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setStock(rs.getInt("stock"));
                product.setUnitPrice(rs.getDouble("unit_price"));
                products.add(product);
            }
            return products;
        });
//		System.out.println(productList);
		return productList;
	}

	@Override
	public Product getProductById(Integer id) {
	    Product prod = jdbcTemplate.execute("{call p_productscreen(?, ?, ?, ?, ?, ?)}", (CallableStatementCallback<Product>) csc -> {
	        csc.setString(1, "SELECT_BY_ID");
	        csc.setInt(2, id);
	        csc.setNull(3, Types.VARCHAR);
	        csc.setNull(4, Types.INTEGER);
	        csc.setNull(5, Types.DOUBLE);
	        csc.registerOutParameter(6, Types.VARCHAR);
	        csc.execute();

	        ResultSet rs = csc.getResultSet();
	        Product product = null;
	        if (rs.next()) {
	            product = new Product();
	            product.setProductId(rs.getInt("product_id"));
	            product.setProductName(rs.getString("product_name"));
	            product.setStock(rs.getInt("stock"));
	            product.setUnitPrice(rs.getDouble("unit_price"));
	        }
	        return product;
	    });
	    System.out.println(prod);
		return prod;
	}


}
