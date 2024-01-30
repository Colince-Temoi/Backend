package tech.csm.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import tech.csm.domain.Biller;
import tech.csm.domain.Transactions;
import tech.csm.util.DBUtil;

public class BillerDaoImpl implements BillerDao {
	private Connection con;
	
	public BillerDaoImpl() {
		con = DBUtil.getConnection();
	}

	@Override
	public String createBill(Biller biller, List<Transactions> transactions) {
//		Because we are doing CRUD operations on two tables, this will happen as a single unit of work: A transaction we refer to this
//		For this purpose we need to set auto-commit to false write our Transaction code then finally we will perform a commit.
		
//		Prepare a query string to insert what we have in Bill Dto into t_biller table
		final String insertBillerQuery = "INSERT INTO pos_schema.t_biller (name,phone, bill_date,total_price) VALUES (?,?,?,?);";
		final String insertTxQuery ="INSERT INTO pos_schema.t_biller_product (biller_id,product_id, num_of_units,total_price) VALUES (?,?,?,?);";
		final String upQuery="update pos_schema.t_product_master set stock=stock - ? where product_id= ?;";
			
//		ready PS object
		try {
			PreparedStatement ps = con.prepareStatement(insertBillerQuery,Statement.RETURN_GENERATED_KEYS);
//			Disable Auto-commit feature
			con.setAutoCommit(false);
			
//			Using ps reference set the values into placeholders of the query string
			
			ps.setString(1, biller.getBillerName());
			ps.setString(2, biller.getPhoneNumber());
			ps.setDate(3, new Date(biller.getBillDate().getTime()));
			ps.setDouble(4, biller.getTotalAmount());
			
//			Execute the things
			int c=ps.executeUpdate();
			
//			Get the generated Bill id
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			
			Integer billNo = rs.getInt(1);
			
//			Insert things into t_transaction table : we need to create a batch
			rs.close();
			ps.close();
//			We need a new ps object to perform insertion of things into t_transaction table
			ps=con.prepareStatement(insertTxQuery,Statement.RETURN_GENERATED_KEYS);
			
//			Am not closing the above ps because I still need to utilize it in some batch processing below.
			
//			We need a new ps object to perform necessary updation of things into t_product_master table
			PreparedStatement psu=con.prepareStatement(upQuery);
			
			for (Transactions x : transactions) {
				
//				Setting values to insert into t_tx table
				ps.setInt(1, billNo);
				ps.setInt(2, x.getProduct().getId());
				ps.setInt(3, x.getNumberofUnits());
				ps.setDouble(4,	x.getTotalAmount());
				
//				Setting values to aid updation in product table
				psu.setInt(1, x.getNumberofUnits());
				psu.setInt(2, x.getProduct().getId());
				
//				Now add the PS objects into a batch
				ps.addBatch();
				psu.addBatch();
			}
			
			int[] respbp=ps.executeBatch();
			System.out.println(Arrays.toString(respbp)+"*******");
			
			int[] resp=psu.executeBatch();
			System.out.println(Arrays.toString(resp)+"*******");
			
			ps.clearBatch();
			psu.clearBatch();
			
			psu.close();
			ps.close();
			
			con.commit();
			return "Transaction complete";
		} catch (SQLException e) {
//			roll back the things if try block things are not successful
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
	}

}
