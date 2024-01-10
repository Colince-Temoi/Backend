package tech.get_tt_right.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import tech.get_tt_right.domain.Employee;
import tech.get_tt_right.util.DbUtil;

public class EmployeeDaoImpl implements EmployeeDao {

//	Primitive dependencies
	private Integer count;
	private String message;
	
//	Secondary Dependencies
	Connection con;
	
	public EmployeeDaoImpl() {
		con = DbUtil.getConnection();
	}
	
	@Override
	public String addEmployee(Employee employee) {
		
		final String insertQuery = "INSERT INTO empcrud_schema.t_emp (name, salary, hire_date,dept_id) VALUES (?, ?, ?,?);";

		try {

			PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

//			DML - Insert record(s)

			Integer numOfInsertedRecords = 0;

//				1. Set the values
			ps.setString(1, employee.getName());
			ps.setDouble(2, employee.getSalary());
			ps.setDate(3, new Date(employee.getHire_date().getTime()));
			ps.setInt(4, employee.getDepartment().getDepartmentId());

//				2. Fire the executeUpdate method of PS
			count = ps.executeUpdate();
			numOfInsertedRecords += count;

//				Get the generated key after the insert is successful
			ResultSet key = ps.getGeneratedKeys();

			key.next();
			message = "\nRecord inserted sucessfully with the id " + key.getInt(1) + "\n";
			
			key.close();
			ps.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

}
