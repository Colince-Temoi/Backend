package tech.csm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tech.csm.domain.Product;
import tech.csm.util.DBUtil;

public class ProductDaoImpl implements Productdao {

	private Connection con;

	public ProductDaoImpl() {
		con = DBUtil.getConnection();
	}

	@Override
	public List<Product> getAllProducts() {
		
//		Query String
		final String selectQuery = "SELECT product_id,product_name, stock, unit_price FROM pos_schema.t_product_master";

//		Will hold the list of products we get from  the DB
		List<Product> products = null;

		try {
			PreparedStatement ps = con.prepareStatement(selectQuery);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				products = new ArrayList<>();
				
				do {
//					Will hold each product we get from  the DB
					Product product = new Product();
					product.setId(rs.getInt(1));
					product.setName(rs.getString(2));
					product.setStock(rs.getInt(3));
					product.setUnitPrice(rs.getDouble(4));
					
					products.add(product);
				} while (rs.next());			

			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return products;
	}

	@Override
	public Product getProductById(Integer i) {
//		Query String
		final String selectQuery = "SELECT product_id,product_name, stock, unit_price FROM pos_schema.t_product_master WHERE product_id = ?";
		Product product = null;
		
		try {
			PreparedStatement ps = con.prepareStatement(selectQuery);
			ps.setInt(1, i);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
//				Will hold each product we get from  the DB
				product = new Product();
				product.setId(rs.getInt(1));
				product.setName(rs.getString(2));
				product.setStock(rs.getInt(3));
				product.setUnitPrice(rs.getDouble(4));
			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return product;
	}

}

