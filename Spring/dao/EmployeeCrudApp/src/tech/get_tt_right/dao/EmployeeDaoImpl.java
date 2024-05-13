package tech.get_tt_right.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import tech.get_tt_right.domain.Department;
import tech.get_tt_right.domain.Employee;
import tech.get_tt_right.util.DbUtil;

public class EmployeeDaoImpl implements EmployeeDao {

//	Primitive dependencies
	private Integer count;
	private String message;
	
	private static final String INSERT_EMPLOYEE_SQL = "INSERT INTO empcrud_schema.t_emp (name, salary, hire_date, dept_id,is_deleted) VALUES (?, ?, ?, ?,?)";


//	Secondary Dependencies
	Connection con;

	public EmployeeDaoImpl() {
		con = DbUtil.getConnection();
	}
	
	
	 @Override
	    public String addEmployee(Employee employee) {
	        String sql = "INSERT INTO empcrud_schema.t_emp (name, salary, hire_date, dept_id) VALUES (?, ?, ?, ?)";

	        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	            ps.setString(1, employee.getName());
	            ps.setDouble(2, employee.getSalary());
	            ps.setDate(3, new java.sql.Date(employee.getHire_date().getTime())); // Assuming hire_date is java.util.Date
	            ps.setInt(4, employee.getDepartment().getDepartmentId());

	            int rowsAffected = ps.executeUpdate();

	            if (rowsAffected > 0) {
	                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        int generatedId = generatedKeys.getInt(1);
	                        return "Record inserted successfully with the id " + generatedId;
	                    } else {
	                        return "Record inserted, but could not retrieve generated ID.";
	                    }
	                }
	            } else {
	                return "Failed to insert record.";
	            }

	        } catch (SQLException e) {
	            e.printStackTrace(); 
	            return "Error during employee insertion: " + e.getMessage();
	        }
	    }
	
	
	 @Override
	    public String updateEmployee(Employee employeeToUpdate) {
	        String message = null;
	        try {
	        	Connection conn = DbUtil.getConnection();
	            PreparedStatement ps = conn.prepareStatement("UPDATE empcrud_schema.t_emp SET name=?, hire_date=?, dept_id=?, is_deleted=? WHERE emp_id=?"); 

	            ps.setString(1, employeeToUpdate.getName());
	            ps.setDate(2, new java.sql.Date(employeeToUpdate.getHire_date().getTime()));
	            ps.setInt(3, employeeToUpdate.getDepartment().getDepartmentId());
	            ps.setString(4, employeeToUpdate.getIsDeleted());
	            ps.setInt(5, employeeToUpdate.getEmpId());
	            
	            int rowCount = ps.executeUpdate();
	            if(rowCount > 0) {
	                message = "Employee updated successfully";
	            } else {
	                message = "Failed to update employee";
	            }
	        } catch (SQLException e) {
	            message = "Error while updating employee: " + e.getMessage();
	        }
	        return message;
	    }
	 
	 
	 @Override
	 public List getEmployeesByDepartment(int deptId) {
	     String sql = "SELECT emp_id, name, salary, hire_date FROM empcrud_schema.t_emp WHERE dept_id = ? AND is_deleted = 'NO'";
	     List employees = new ArrayList<>();
	     
	     try (PreparedStatement ps = con.prepareStatement(sql)) {
	         ps.setInt(1, deptId);
	         
	         try (ResultSet rs = ps.executeQuery()) {
	             while (rs.next()) {
	                 Employee employee = new Employee();
	                 employee.setEmpId(rs.getInt("emp_id"));
	                 employee.setName(rs.getString("name"));
	                 employee.setSalary(rs.getDouble("salary"));
	                 employee.setHire_date(rs.getDate("hire_date"));
	                 
	                 Department department = new Department();
	                 department.setDepartmentId(deptId);
	                 employee.setDepartment(department);
	                 
	                 employees.add(employee);
	             }
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	     }
	     
	     return employees;
	 }
	 
	 @Override
	 public List getEmployeesBySalary(double salary) {
	     List employees = new ArrayList<>();
	     
	     try (PreparedStatement ps = con.prepareStatement("SELECT * FROM empcrud_schema.t_emp WHERE salary >= ?")) {
	         ps.setDouble(1, salary);
	         
	         try (ResultSet rs = ps.executeQuery()) {
	             while (rs.next()) {
	                 Employee employee = new Employee();
	                 
	                 employee.setEmpId(rs.getInt("emp_id"));
	                 employee.setName(rs.getString("name"));
	                 employee.setHire_date(rs.getDate("hire_date"));
	                 employee.setSalary(rs.getDouble("salary"));
	                 employee.setIsDeleted(rs.getString("is_deleted"));
	                 
	                 Department department = new Department();
	                 department.setDepartmentId(rs.getInt("dept_id"));
	                 // Here we assume that the department name is stored in a separate table.
	                 // You would need to perform another query to retrieve the department name.
	                 // For simplicity, let's leave it out for now.
	                 employee.setDepartment(department);
	                 
	                 employees.add(employee);
	             }
	         }
	     } catch (SQLException e) {
	         e.printStackTrace();
	     }
	     
	     return employees;
	 }
	
	
	 @Override
	    public List<Employee> getAllEmployees() {
	        List<Employee> employees = new ArrayList<>();
	        String sql = "SELECT emp_id, name, salary, hire_date, dept_id FROM empcrud_schema.t_emp WHERE is_deleted = 'NO'";  

	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
	                Employee employee = new Employee();
	                employee.setEmpId(rs.getInt("emp_id"));
	                employee.setName(rs.getString("name"));
	                employee.setSalary(rs.getDouble("salary"));
	                employee.setHire_date(rs.getDate("hire_date")); 

	                // Assuming you have a Department class to set
	                Department department = new Department();
	                department.setDepartmentId(rs.getInt("dept_id")); 
	                employee.setDepartment(department); 

	                employees.add(employee);
	            }

	        } catch (SQLException e) {
	            // Handle the exception appropriately
	            e.printStackTrace();
	        }

	        return employees;
	    }


//	@Override
//	public String addEmployee(Employee employee) {
//		System.out.println(employee);
//
//		final String insertQuery = "INSERT INTO empcrud_schema.t_emp (name,salary, hire_date,dept_id,is_deleted) VALUES (?,?,?,?,?);";
//
//		try {
//
//			PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
//
////			DML - Insert record(s)
//
//			Integer numOfInsertedRecords = 0;
//
////				1. Set the values
//			ps.setString(1, employee.getName());
//			ps.setDouble(2, employee.getSalary());
//			ps.setDate(3, new Date(employee.getHire_date().getTime()));
//			ps.setInt(4, employee.getDepartment().getDepartmentId());
//			ps.setString(5, employee.getIsDeleted());
//
////				2. Fire the executeUpdate method of PS
//			count = ps.executeUpdate();
//			numOfInsertedRecords += count;
//
////				Get the generated key after the insert is successful
//			ResultSet key = ps.getGeneratedKeys();
//
//			key.next();
//			message = "\nRecord inserted sucessfully with the id " + key.getInt(1) + "\n";
//
//			key.close();
//			ps.close();
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return message;
//	}

//	@Override
//	public List<Employee> getAllEmployees() {
//		final String selectQuery = "SELECT emp_id,name, salary, hire_date,dept_id FROM empcrud_schema.t_emp WHERE is_deleted = 'NO';";
//
//		List<Employee> employeeList = new ArrayList<>();
//
//		try {
//			PreparedStatement ps = con.prepareStatement(selectQuery);
//			ResultSet rs = ps.executeQuery();
//
//			while (rs.next()) {
//				Employee employee = new Employee();
////	            Department department = new Department();
//				employee.setEmpId(rs.getInt("emp_id"));
//				employee.setName(rs.getString("name"));
//				employee.setSalary(rs.getDouble("salary"));
//				employee.setHire_date(rs.getDate("hire_date"));
////	            employee.setDepartment(department.setDepartmentId(rs.getInt("dept_id")));
//
//				// Set only the department ID
//				Department department = new Department();
//				department.setDepartmentId(rs.getInt("dept_id"));
//				employee.setDepartment(department);
//
//				employeeList.add(employee);
//			}
//
//			rs.close();
//			ps.close();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return employeeList;
//	}
	
	
	@Override
	public Employee getEmployeeById(int employeeId) {
	    Employee employee = null;

	    try {
	        String selectQuery = "SELECT emp_id, name, salary, hire_date, dept_id, is_deleted FROM empcrud_schema.t_emp WHERE emp_id = ? AND is_deleted = 'NO'";
	        PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
	        preparedStatement.setInt(1, employeeId);
	        ResultSet rs = preparedStatement.executeQuery();
	        
	        while (rs.next()) {
	            employee = new Employee();
	            employee.setEmpId(rs.getInt("emp_id"));
	            employee.setName(rs.getString("name"));
	            employee.setSalary(rs.getDouble("salary"));
	            employee.setHire_date(rs.getDate("hire_date"));                
	            int deptId = rs.getInt("dept_id");
	            Department department = new Department();
	            department.setDepartmentId(deptId);
	            employee.setDepartment(department);
	            employee.setIsDeleted(rs.getString("is_deleted"));  
	        }
	    } catch (SQLException e) {
	        throw new RuntimeException("Error retrieving employee by id", e);
	    } finally {
//	        DbUtil.closeConnection();
	    }
	    return employee;
	}

//	@Override
//	public Employee getEmployeeById(int employeeId) {
//		final String selectQuery = "SELECT emp_id, name, salary, hire_date, dept_id FROM empcrud_schema.t_emp WHERE emp_id = ? AND is_deleted = 'NO'";
//		Employee employee = null;
//
//		try {
//			PreparedStatement ps = con.prepareStatement(selectQuery);
//			ps.setInt(1, employeeId);
//
//			ResultSet rs = ps.executeQuery();
//
//			if (rs.next()) {
//				employee = new Employee();
//				employee.setEmpId(rs.getInt("emp_id"));
//				employee.setName(rs.getString("name"));
//				employee.setSalary(rs.getDouble("salary"));
//				employee.setHire_date(rs.getDate("hire_date"));
//
//				// Set only the department ID
//				Department department = new Department();
//				department.setDepartmentId(rs.getInt("dept_id"));
//				employee.setDepartment(department);
//			}
//
//			rs.close();
//			ps.close();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return employee;
//	}

//	@Override
//	public String updateEmployee(Employee employee) {
//		final String updateQuery = "UPDATE empcrud_schema.t_emp SET name = ?, salary = ?, hire_date = ?, dept_id = ?, is_deleted = ? WHERE emp_id = ?";
//
//		try {
//			PreparedStatement ps = con.prepareStatement(updateQuery);
//			ps.setString(1, employee.getName());
//			ps.setDouble(2, employee.getSalary());
//			ps.setDate(3, new Date(employee.getHire_date().getTime()));
//			ps.setInt(4, employee.getDepartment().getDepartmentId());
//			ps.setString(5, employee.getIsDeleted());
//			ps.setInt(6, employee.getEmpId());
//
//			int count = ps.executeUpdate();
//			if (count > 0) {
//				return "Employee updated successfully.";
//			} else {
//				return "Failed to update employee.";
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return "Error updating employee.";
//		}
//	}

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

	@Override
	public List<Employee> getEmployeesByHireDate(java.util.Date formattedDate) {
	    final String selectQuery = "SELECT emp_id, name, salary, hire_date, dept_id FROM empcrud_schema.t_emp WHERE hire_date >= ? AND is_deleted = 'NO';";
	    List<Employee> employeeList = new ArrayList<>();

	    try {
	        PreparedStatement ps = con.prepareStatement(selectQuery);
	        ps.setDate(1, new Date(formattedDate.getTime()));

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Employee employee = new Employee();
	            employee.setEmpId(rs.getInt("emp_id"));
	            employee.setName(rs.getString("name"));
	            employee.setSalary(rs.getDouble("salary"));
	            employee.setHire_date(rs.getDate("hire_date"));

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





//	@Override
//	public List<Employee> getEmployeesBySalary(double salary) {
//	    final String selectQuery = "SELECT emp_id, name, salary, hire_date, dept_id FROM empcrud_schema.t_emp WHERE salary >= ? AND is_deleted = 'NO';";
//	    List<Employee> employeeList = new ArrayList<>();
//
//	    try {
//	        PreparedStatement ps = con.prepareStatement(selectQuery);
//	        ps.setDouble(1, salary);
//
//	        ResultSet rs = ps.executeQuery();
//
//	        while (rs.next()) {
//	            Employee employee = new Employee();
//	            employee.setEmpId(rs.getInt("emp_id"));
//	            employee.setName(rs.getString("name"));
//	            employee.setSalary(rs.getDouble("salary"));
//	            employee.setHire_date(rs.getDate("hire_date"));
//
//	            // Set only the department ID
//	            Department department = new Department();
//	            department.setDepartmentId(rs.getInt("dept_id"));
//	            employee.setDepartment(department);
//
//	            employeeList.add(employee);
//	        }
//
//	        rs.close();
//	        ps.close();
//
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//
//	    return employeeList;
//	}

//	@Override
//	public List<Employee> getEmployeesByDepartment(int deptId) {
//	    final String selectQuery = "SELECT emp_id, name, salary, hire_date, dept_id FROM empcrud_schema.t_emp WHERE dept_id = ? AND is_deleted = 'NO';";
//	    List<Employee> employeeList = new ArrayList<>();
//
//	    try {
//	        PreparedStatement ps = con.prepareStatement(selectQuery);
//	        ps.setInt(1, deptId);
//
//	        ResultSet rs = ps.executeQuery();
//
//	        while (rs.next()) {
//	            Employee employee = new Employee();
//	            employee.setEmpId(rs.getInt("emp_id"));
//	            employee.setName(rs.getString("name"));
//	            employee.setSalary(rs.getDouble("salary"));
//	            employee.setHire_date(rs.getDate("hire_date"));
//
//	            // Set only the department ID
//	            Department department = new Department();
//	            department.setDepartmentId(rs.getInt("dept_id"));
//	            employee.setDepartment(department);
//
//	            employeeList.add(employee);
//	        }
//
//	        rs.close();
//	        ps.close();
//
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//
//	    return employeeList;
//	}



}
