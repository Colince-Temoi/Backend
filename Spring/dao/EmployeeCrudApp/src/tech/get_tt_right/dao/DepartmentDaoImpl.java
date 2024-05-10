package tech.get_tt_right.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tech.get_tt_right.domain.Department;
import tech.get_tt_right.util.DbUtil;

public class DepartmentDaoImpl implements DepartmentDao {

//	Secondary dependencies
	Connection con;
	List<Department> departments;

	public DepartmentDaoImpl() {
		con = DbUtil.getConnection();

	}
	
	 @Override
	    public List<Department> getAllDepartments() {
	        List<Department> departments = new ArrayList<>();
	        String query = "SELECT dept_id, dept_name FROM t_dept";

	        try (PreparedStatement pstmt = con.prepareStatement(query);
	             ResultSet rs = pstmt.executeQuery()) {

	            while (rs.next()) {
	                Department department = new Department();
	                department.setDepartmentId(rs.getInt("dept_id"));
	                department.setDepartmentName(rs.getString("dept_name"));
	                departments.add(department);
	            }

	        } catch (SQLException e) {
	            // Handle the exception appropriately (e.g., log the error)
	            e.printStackTrace(); 
	        }
//	        departments.forEach(x->System.out.println(x));

	        return departments;
	    }
	 @Override
	 public Department getDepartmentById(Integer id) {
	     String query = "SELECT dept_id, dept_name FROM t_dept WHERE dept_id = ?";

	     try (PreparedStatement pstmt = con.prepareStatement(query)) {
	         pstmt.setInt(1, id);
	         try (ResultSet rs = pstmt.executeQuery()) {
	             if (rs.next()) {
	                 Department department = new Department();
	                 department.setDepartmentId(rs.getInt("dept_id"));
	                 department.setDepartmentName(rs.getString("dept_name"));
	                 return department;
	             } 
	         }
	     } catch (SQLException e) {
	         // Handle the exception appropriately (e.g., log the error)
	         e.printStackTrace(); 
	     }

	     return null; // Department not found
	 }
//	@Override
//	public List<Department> getAllDepartments() {
////		Query String
//		final String selectAllQuery = "SELECT dept_id,dept_name FROM empcrud_schema.t_dept;";
//
////		PreparedStatement
//		try {
//			PreparedStatement ps = con.prepareStatement(selectAllQuery);
//
//			ResultSet resultSet = ps.executeQuery();
//
//			if (resultSet.next()) {
////				Prepare a place to store the received list of Departments details
//				departments = new ArrayList<>();
//				do {
////					Prepare Department object to receive each Department details.
//					Department department = new Department();
//
//					department.setDepartmentId(resultSet.getInt(1));
//					department.setDepartmentName(resultSet.getString(2));
//
//					departments.add(department);
//
//				} while (resultSet.next());
//			}
////			No need to explicitly write the else part here.
//
//			ps.close();
//
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		}
//
//		return departments;
//	}

//	@Override
//	public Department getDepartmentById(Integer id) {
//	    // Query String
//	    final String selectByIdQuery = "SELECT dept_id, dept_name FROM empcrud_schema.t_dept WHERE dept_id = ?;";
//
//	    // PreparedStatement
//	    try {
//	        PreparedStatement ps = con.prepareStatement(selectByIdQuery);
//	        ps.setInt(1, id);
//
//	        ResultSet resultSet = ps.executeQuery();
//
//	        if (resultSet.next()) {
//	            // Prepare Department object to receive the Department details.
//	            Department department = new Department();
//
//	            department.setDepartmentId(resultSet.getInt(1));
//	            department.setDepartmentName(resultSet.getString(2));
//
//	            ps.close();
//	            return department;
//	        } else {
//	            ps.close();
//	            return null; // Department with the given ID not found.
//	        }
//
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	        return null;
//	    }
//	}



}
