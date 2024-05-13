package com.get_tt_right.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.get_tt_right.domain.Biller;
import com.get_tt_right.domain.Product;
import com.get_tt_right.domain.Transactions;

import jakarta.annotation.PostConstruct;

@Repository
public class BillerDaoImpl implements BillerDao {		
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SimpleJdbcCall createBillProcedure;
	private SimpleJdbcCall getBillProcedure;
	
	private Integer billerId;

	@PostConstruct
	private void init() {
	createBillProcedure = new SimpleJdbcCall(jdbcTemplate.getDataSource())
	            .withProcedureName("p_biller")
	            .declareParameters(
	                    new SqlParameter("p_action", Types.VARCHAR),
	                    new SqlParameter("p_biller_id", Types.INTEGER),
	                    new SqlParameter("p_name", Types.VARCHAR),                    
	                    new SqlParameter("p_phone", Types.VARCHAR),
	                    new SqlParameter("p_bill_date", Types.TIMESTAMP), // Assuming billDate is java.util.Date
	                    new SqlParameter("p_total_price", Types.DECIMAL),
	                    new SqlOutParameter("o_biller_id", Types.INTEGER),
	                    new SqlOutParameter("o_message", Types.VARCHAR)
	            );
	 getBillProcedure = new SimpleJdbcCall(jdbcTemplate.getDataSource())
             .withProcedureName("p_biller_product")
             .declareParameters(
	            		new SqlParameter("p_action", Types.VARCHAR),
						new SqlParameter("p_transaction_id", Types.INTEGER), new SqlParameter("p_product_id", Types.INTEGER),
						new SqlParameter("p_num_of_units", Types.INTEGER), new SqlParameter("p_total_price", Types.DECIMAL),
						new SqlParameter("p_biller_id", Types.INTEGER),
						new SqlOutParameter("o_transaction_id", Types.INTEGER), new SqlOutParameter("o_message", Types.VARCHAR)
             )
             .returningResultSet("transactions", new RowMapper<Transactions>() {
                 @Override
                 public Transactions mapRow(ResultSet rs, int rowNum) throws SQLException {
                     // Create Biller object only if it's a new biller_id
                     Biller biller = null;
                     if (biller == null || rs.getInt("biller_id") != biller.getBillerId()) {
                         biller = mapBiller(rs);
                     }

                     // Create a Product object
                     Product product = mapProduct(rs);

                     // Create a Transaction object
                     Transactions transaction = mapTransaction(rs, biller, product);

                     return transaction;
                 }
             });
	    
	}

	@Override
	public String createBill(List<Transactions> transactionsDto) {
	    Transactions firstTransaction = transactionsDto.get(0); // Assuming all have the same Biller data
	    Biller biller = firstTransaction.getBillerVo();

	    MapSqlParameterSource inParams = new MapSqlParameterSource()
	            .addValue("p_action", "Insert")
	            .addValue("p_biller_id", 0)
	            .addValue("p_name", biller.getBillerName())
	            .addValue("p_phone", biller.getPhoneNumber())
	            .addValue("p_bill_date", biller.getBillDate())
	            .addValue("p_total_price", biller.getTotalAmount());

	    Map<String, Object> outParams = createBillProcedure.execute(inParams);
	    
	    billerId = (int) outParams.get("o_biller_id"); // Extract billerId from the result 
	    
	    // Prepare for batch insert into t_biller_product
	    SimpleJdbcInsert insertBillerProduct = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
	            .withTableName("t_biller_product")
	            .usingGeneratedKeyColumns("transaction_id");

	    List<SqlParameterSource> parameters = new ArrayList<>();
	    for (Transactions transaction : transactionsDto) {
	        parameters.add(new MapSqlParameterSource()
	                  .addValue("product_id", transaction.getProduct().getProductId())
	                  .addValue("num_of_units", transaction.getNumberofUnits())
	                  .addValue("total_price", transaction.getTotalAmount())
	                  .addValue("biller_id", billerId));
	    }

	    int[] updateCounts = insertBillerProduct.executeBatch(parameters.toArray(new SqlParameterSource[0]));
	    System.out.println("Rows inserted: " + Arrays.toString(updateCounts)); // Log the insert results
	   

	    return "Bill Created"; // Update the return value as needed
	}
	
	 @Override
	    public List<Transactions> getBill() {
	        SqlParameterSource inParams = new MapSqlParameterSource()
					.addValue("p_action", "Select", Types.VARCHAR).addValue("p_transaction_id", 0, Types.INTEGER)
					.addValue("p_product_id", 0, Types.INTEGER).addValue("p_num_of_units", 0, Types.INTEGER)
					.addValue("p_total_price", 0, Types.DECIMAL).addValue("p_biller_id", billerId, Types.INTEGER);

	        Map<String, Object> outParams = getBillProcedure.execute(inParams);
//	        String message = (String) outParams.get("o_message");
//	        System.out.println(message);
//
	        List<Transactions> transactions = (List<Transactions>) outParams.get("transactions");
//	        for (Transactions transaction : transactions) {
//	            System.out.println(transaction);
//	        }

	        return transactions;
	    }
	

//	@Override
//	public List<Transactions> getBill() {
//	    SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
//	            .withProcedureName("p_biller_product")
//	            .declareParameters(
//	            		new SqlParameter("p_action", Types.VARCHAR),
//						new SqlParameter("p_transaction_id", Types.INTEGER), new SqlParameter("p_product_id", Types.INTEGER),
//						new SqlParameter("p_num_of_units", Types.INTEGER), new SqlParameter("p_total_price", Types.DECIMAL),
//						new SqlParameter("p_biller_id", Types.INTEGER),
//						new SqlOutParameter("o_transaction_id", Types.INTEGER), new SqlOutParameter("o_message", Types.VARCHAR))
//	            .returningResultSet("transactionResult", new RowMapper<Transactions>() {
//	                @Override
//	                public Transactions mapRow(ResultSet rs, int rowNum) throws SQLException {
//	                    // Map the ResultSet rows to Transaction objects as before
//	                    Biller biller = null;
//	                    if (biller == null || rs.getInt("biller_id") != biller.getBillerId()) {
//	                        biller = mapBiller(rs);
//	                    }
//
//	                    Product product = mapProduct(rs);
//	                    Transactions transaction = mapTransaction(rs, biller, product);
//	                    return transaction;
//	                }
//	            });
//	    
//		MapSqlParameterSource msps = new MapSqlParameterSource()
//				.addValue("p_action", "Select", Types.VARCHAR).addValue("p_transaction_id", 0, Types.INTEGER)
//				.addValue("p_product_id", 0, Types.INTEGER).addValue("p_num_of_units", 0, Types.INTEGER)
//				.addValue("p_total_price", 0, Types.DECIMAL).addValue("p_biller_id", 5, Types.INTEGER);
//	    
//
//	    // Execute the stored procedure with no input parameters since we are fetching all transactions
//	    Map<String, Object> resultMap = simpleJdbcCall.execute(msps);
//
//	    // Extract the list of Transactions from the result map
//	    List<Transactions> transactionsList = (List<Transactions>) resultMap.get("transactionResult");
//
//	    for (Transactions transaction : transactionsList) {
//	        System.out.println(transaction);
//	    }
//
//	    return transactionsList;
//	}
	
//	@Override
//	public List<Transactions> getBill() {
//	    String sql = "SELECT t.transaction_id, bv.biller_id, bv.name, bv.phone, bv.bill_date, " +
//        "       bv.total_price, p.product_id, p.product_name, p.unit_price, t.num_of_units " + 
//        "FROM t_biller_product t " +
//        "JOIN t_biller bv ON bv.biller_id = t.biller_id " +
//        "JOIN t_product_master p ON t.product_id = p.product_id "; 
//        // Add a WHERE clause if you need to filter bills
//
//	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
//	        // Create Biller object only if it's a new biller_id
//	        Biller biller = null;
//	        if (biller == null || rs.getInt("biller_id") != biller.getBillerId()) {
//	            biller = mapBiller(rs);
//	        }
//
//	        // Create a Product object
//	        Product product = mapProduct(rs);
//
//	        // Create a Transaction object
//	        Transactions transaction = mapTransaction(rs, biller, product);
//	                
//	        System.out.println(transaction);
//
//	        return transaction;
//	    });
//	}
//
	// Helper methods to map ResultSet rows to objects
	private Biller mapBiller(ResultSet rs) throws SQLException {
	    Biller biller = new Biller();
	    biller.setBillerId(rs.getInt("biller_id"));
	    biller.setBillerName(rs.getString("name"));
	    biller.setPhoneNumber(rs.getString("phone"));
	    biller.setBillDate(rs.getDate("bill_date"));
	    biller.setTotalAmount(rs.getDouble("total_price"));
	    return biller;
	}

	private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));
        product.setUnitPrice(rs.getDouble("unit_price"));
		return product;
	}

	private Transactions mapTransaction(ResultSet rs, Biller biller, Product product) throws SQLException {
        Transactions transaction = new Transactions();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setNumberofUnits(rs.getInt("num_of_units"));
        transaction.setBillerVo(biller);
        transaction.setProduct(product);
        // No need to set totalAmount, as that can be calculated on the fly

        return transaction;
	}
}
