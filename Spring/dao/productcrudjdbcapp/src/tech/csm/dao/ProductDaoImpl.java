package tech.csm.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tech.csm.domain.Product;
import tech.csm.util.DbUtil;

public class ProductDaoImpl implements ProductDao {
	final String selectByIdQuery = "SELECT product_id,product_name,unit_price,mfg_date FROM productjdbc.t_product where product_id=?";
//	final String selectAllQuery = "SELECT product_id,product_name,unit_price,mfg_date FROM productjdbc.t_product where unit_price>?";
	final String selectAllQuery = "SELECT product_id,product_name,unit_price,mfg_date FROM productjdbc.t_product";
	final String countQuery = "select count(*) from productjdbc.t_product where unit_price>?";
	final String insertQuery = "INSERT INTO productjdbc.t_product (product_name, unit_price, mfg_date) VALUES (?, ?, ?)";

	private Integer count;
	private String message;
	Product product = new Product();

	@Override
	public String saveProduct(Product product) {

//		Scanner sc = new Scanner(System.in);

//		get Db connection
		try {

			Connection con = DbUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
			PreparedStatement psCount = con.prepareStatement(countQuery);

//			Test if connection is established
//			System.out.println(con);

			/*
			 * // Drl - Read one record by id!
			 * 
			 * System.out.println("Enter id to product you want outputed"); Integer id =
			 * sc.nextInt();
			 * 
			 * // Replace the question mark with a real value now. ps.setInt(1, id);
			 * 
			 * // Invoking the DRL method on top of ps reference ResultSet rs =
			 * ps.executeQuery();
			 * 
			 * while (rs.next()) {
			 * System.out.println(rs.getInt(1)+" | "+rs.getString(2)+" | "+rs.getDouble(3)
			 * +" | "+rs.getString(4)); }
			 */

//			DML - Insert record(s)

			Integer numOfInsertedRecords = 0;

//				1. Set the values
			ps.setString(1, product.getProductName());
			ps.setDouble(2, product.getUnitPrice());
			ps.setDate(3, new Date(product.getMfgDate().getTime()));

//				2. Fire the executeUpdate method of PS
			count = ps.executeUpdate();
			numOfInsertedRecords += count;

//				Get the generated key after the insert is successful
			ResultSet key = ps.getGeneratedKeys();

			key.next();
			message = "\nRecord inserted sucessfully with the id " + key.getInt(1) + "\n";

			// Drl - Read all records!

			/*
			 * System.out.
			 * println("Enter the unit price to that you want match the selection criteria"
			 * ); Double price = sc.nextDouble();
			 * 
			 * // Replace the question mark with a real value now. ps.setDouble(1, price);
			 * psCount.setDouble(1, price);
			 * 
			 * // Invoking the DRL method on top of ps reference ResultSet rs =
			 * ps.executeQuery(); ResultSet rsCount = psCount.executeQuery();
			 * 
			 * Integer count = 0;
			 * 
			 * while (rsCount.next()) { count = rsCount.getInt(1);
			 * 
			 * }
			 * 
			 * if (rs.next()) {
			 * System.out.println("\n-----"+count+" records found!----------- \n"); do {
			 * System.out.println( rs.getInt(1) + " | " + rs.getString(2) + " | " +
			 * rs.getDouble(3) + " | " + rs.getString(4)); } while (rs.next()); } else {
			 * System.out.println("\n-------"+count+" record(s) found with price > "+price+
			 * "---------\n"); }
			 */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	@Override
	public Product getProductById(Integer id) {
		Product p = null;
		try {
			Connection con = DbUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(selectByIdQuery);
//			Replace the placeholder in the query string with actual value
			ps.setInt(1, id);
//			invoke excuteQuery() method on top of ps object reference
			ResultSet rs = ps.executeQuery();
//			Covert the rs object into Product dto-Nothing but; set the rs object values into Product dto type

			if (rs.next()) {
				product.setProductId(rs.getInt(1));
				product.setProductName(rs.getString(2));
				product.setUnitPrice(rs.getDouble(3));
				product.setMfgDate(rs.getDate(4));

				p = product;
			}
//			Close resources once done using them.
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;

	}

	@Override
	public List<Product> getAllProducts() {
		Connection con = DbUtil.getConnection();
		List<Product> productList = null;

		try {
			PreparedStatement ps = con.prepareStatement(selectAllQuery);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				productList = new ArrayList<>();
				do {
					Product p = new Product();
					p.setProductId(rs.getInt(1));
					p.setProductName(rs.getString(2));
					p.setUnitPrice(rs.getDouble(3));
					p.setMfgDate(rs.getDate(4));
					productList.add(p);

				} while (rs.next());
			}

			ps.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return productList;
	}

	@Override
	public String updateProduct(Product existingProduct) {
		String message = null;

		try {
			Connection con = DbUtil.getConnection();
			String updateQuery = "UPDATE productjdbc.t_product SET product_name=?, unit_price=?, mfg_date=? WHERE product_id=?";
			PreparedStatement ps = con.prepareStatement(updateQuery);

			ps.setString(1, existingProduct.getProductName());
			ps.setDouble(2, existingProduct.getUnitPrice());
			ps.setDate(3, new Date(existingProduct.getMfgDate().getTime()));
			ps.setInt(4, existingProduct.getProductId());

			int count = ps.executeUpdate();

			if (count > 0) {
				message = "Product with ID " + existingProduct.getProductId() + " updated successfully!";
			} else {
				message = "Product with ID " + existingProduct.getProductId() + " not found or not updated!";
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return message;
	}

	@Override
	public String deleteProduct(Product existingProduct) {
		String message = null;

		try {
			Connection con = DbUtil.getConnection();
			String deleteQuery = "DELETE FROM productjdbc.t_product WHERE product_id=?";
			PreparedStatement ps = con.prepareStatement(deleteQuery);

			ps.setInt(1, existingProduct.getProductId());

			int count = ps.executeUpdate();

			if (count > 0) {
				message = "Product with ID " + existingProduct.getProductId() + " deleted successfully!";
			} else {
				message = "Product with ID " + existingProduct.getProductId() + " not found or not deleted!";
			}

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return message;
	}

}
