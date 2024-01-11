package tech.get_tt_right.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tech.get_tt_right.domain.Department;
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

		final String insertQuery = "INSERT INTO empcrud_schema.t_emp (name, salary, hire_date,dept_id,is_deleted) VALUES (?, ?, ?,?,?);";

		try {

			PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

//			DML - Insert record(s)

			Integer numOfInsertedRecords = 0;

//				1. Set the values
			ps.setString(1, employee.getName());
			ps.setDouble(2, employee.getSalary());
			ps.setDate(3, new Date(employee.getHire_date().getTime()));
			ps.setInt(4, employee.getDepartment().getDepartmentId());
			ps.setString(5, employee.getIsDeleted());

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

	@Override
	public List<Employee> getAllEmployees() {
		final String selectQuery = "SELECT emp_id,name, salary, hire_date,dept_id FROM empcrud_schema.t_emp;";

		List<Employee> employeeList = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement(selectQuery);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Employee employee = new Employee();
//	            Department department = new Department();
				employee.setEmpId(rs.getInt("emp_id"));
				employee.setName(rs.getString("name"));
				employee.setSalary(rs.getDouble("salary"));
				employee.setHire_date(rs.getDate("hire_date"));
//	            employee.setDepartment(department.setDepartmentId(rs.getInt("dept_id")));

				// Set only the department ID
				Department department = new Department();
				department.setDepartmentId(rs.getInt("dept_id"));
				employee.setDepartment(department);

				employeeList.add(employee);
			}

			rs.close();
			ps.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employeeList;
	}

	@Override
	public Employee getEmployeeById(int employeeId) {
	    final String selectQuery = "SELECT emp_id, name, salary, hire_date, dept_id FROM empcrud_schema.t_emp WHERE emp_id = ?;";
	    Employee employee = null;

	    try {
	        PreparedStatement ps = con.prepareStatement(selectQuery);
	        ps.setInt(1, employeeId);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            employee = new Employee();
	            employee.setEmpId(rs.getInt("emp_id"));
	            employee.setName(rs.getString("name"));
	            employee.setSalary(rs.getDouble("salary"));
	            employee.setHire_date(rs.getDate("hire_date"));

	            // Set only the department ID
	            Department department = new Department();
	            department.setDepartmentId(rs.getInt("dept_id"));
	            employee.setDepartment(department);
	        }

	        rs.close();
	        ps.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return employee;
	}

	@Override
	public String updateEmployee(Employee employee) {
	    final String updateQuery = "UPDATE empcrud_schema.t_emp SET name = ?, salary = ?, hire_date = ?, dept_id = ?, is_deleted = ? WHERE emp_id = ?;";

	    try {
	        PreparedStatement ps = con.prepareStatement(updateQuery);
	        ps.setString(1, employee.getName());
	        ps.setDouble(2, employee.getSalary());
	        ps.setDate(3, new Date(employee.getHire_date().getTime()));
	        ps.setInt(4, employee.getDepartment().getDepartmentId());
	        ps.setString(5, employee.getIsDeleted());
	        ps.setInt(6, employee.getEmpId());

	        int count = ps.executeUpdate();
	        if (count > 0) {
	            return "Employee updated successfully.";
	        } else {
	            return "Failed to update employee.";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error updating employee.";
	    }
	}

	@Override
	public String deleteEmployee(int deleteEmployeeId) {
	    final String updateQuery = "UPDATE empcrud_schema.t_emp SET is_deleted = 'YES' WHERE emp_id = ?;";

	    try {
	        PreparedStatement ps = con.prepareStatement(updateQuery);
	        ps.setInt(1, deleteEmployeeId);

	        int count = ps.executeUpdate();
	        if (count > 0) {
	            return "Employee with ID " + deleteEmployeeId + " soft deleted successfully.";
	        } else {
	            return "Failed to soft delete employee. Employee with ID " + deleteEmployeeId + " not found.";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error soft deleting employee.";
	    }
	}




}
