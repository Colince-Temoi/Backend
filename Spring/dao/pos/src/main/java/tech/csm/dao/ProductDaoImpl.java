package tech.csm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tech.csm.domain.Product;
import tech.csm.util.DBUtil;

public class ProductDaoImpl implements ProductDao {

	private Connection con;
	public ProductDaoImpl() {
		con=DBUtil.getConnection();
	}
	
	@Override
	public List<Product> getAllProducts() {
		final String seQuery="select product_id,product_name,stock,unit_price from t_product_master";
		List<Product> productList=null;
		try{
			PreparedStatement ps=con.prepareStatement(seQuery);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				productList=new ArrayList<>();
				do {
					Product p=new Product();
					p.setProuctId(rs.getInt(1));
					p.setName(rs.getString(2));
					p.setQnty(rs.getInt(3));
					p.setUnitPrice(rs.getDouble(4));
					productList.add(p);
				}while(rs.next());
			}
			
		}catch(SQLException sq) {
			sq.printStackTrace();
		}
		
		
		return productList;
	}

	@Override
	public Product getProductById(Integer pId) {
		final String seQuery="select product_id,product_name,stock,unit_price from t_product_master where product_id=?";
		Product p=null;
		try{
			PreparedStatement ps=con.prepareStatement(seQuery);
			ps.setInt(1, pId.intValue());
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
					p=new Product();
					p.setProuctId(rs.getInt(1));
					p.setName(rs.getString(2));
					p.setQnty(rs.getInt(3));
					p.setUnitPrice(rs.getDouble(4));
					
			}
			
		}catch(SQLException sq) {
			sq.printStackTrace();
		}
		return p;
	}

}
