package tech.csm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import tech.csm.domain.Bill;
import tech.csm.domain.BillProduct;
import tech.csm.util.DBUtil;

public class BillDaoImpl implements BillDao {

	private Connection con;
	public BillDaoImpl() {
		con=DBUtil.getConnection();
	}
	
	@Override
	public String createBill(Bill bill, List<BillProduct> bpList) {
		final String inQuery="insert into t_biller ( name,phone, bill_date, total_price) values(?,?,?,?)";
		final String inQuery1="insert into t_biller_product (biller_id, product_id, num_of_units) values(?,?,?)";
		final String upQuery="update t_product_master set stock=stock - ? where product_id= ?";
		
		try {
			con.setAutoCommit(false);
			PreparedStatement ps=con.prepareStatement(inQuery,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, bill.getCName());
			ps.setString(2, bill.getCPhone());
			ps.setDate(3, new java.sql.Date(bill.getBillDate().getTime()));
			ps.setDouble(4, bill.getTotalAmount());
			int c=ps.executeUpdate();
			ResultSet rs=ps.getGeneratedKeys();
			rs.next();
			int billNo=rs.getInt(1);
			rs.close();
			ps.close();
			ps=con.prepareStatement(inQuery1,Statement.RETURN_GENERATED_KEYS);
			PreparedStatement psu=con.prepareStatement(upQuery);
			for(BillProduct x:bpList) {
				ps.setInt(1, billNo);
				ps.setInt(2, x.getProduct().getProuctId().intValue());
				ps.setInt(3, x.getNoOfUnits().intValue());
				
				psu.setInt(1, x.getNoOfUnits().intValue());
				psu.setInt(2, x.getProduct().getProuctId().intValue());
				
				psu.addBatch();
				ps.addBatch();
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
		}catch(SQLException se) {
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			se.printStackTrace();
		}
		
		return null;
		
	}

}
