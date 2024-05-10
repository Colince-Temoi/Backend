package tech.csm.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import tech.csm.domain.Department;
import tech.csm.domain.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {
	
    // Dummy data initialization in constructor
    private List<Employee> employees;

    public EmployeeDaoImpl() {
        this.employees = new ArrayList<>();

        // Adding dummy employees data
        this.employees.add(new Employee(1, "John Doe", 50000.0, "2022-01-01", new Department(1, "IT")));
        this.employees.add(new Employee(2, "Jane Smith", 60000.0, "2022-02-01", new Department(2, "HR")));
        this.employees.add(new Employee(3, "Michael Johnson", 70000.0, "2022-03-01", new Department(3, "Finance")));
        this.employees.add(new Employee(4, "Emily Brown", 55000.0, "2022-04-01", new Department(1, "IT")));
        this.employees.add(new Employee(5, "David Wilson", 65000.0, "2022-05-01", new Department(2, "HR")));
        this.employees.add(new Employee(6, "Jessica Martinez", 75000.0, "2022-06-01", new Department(3, "Finance")));
        this.employees.add(new Employee(7, "Christopher Anderson", 60000.0, "2022-07-01", new Department(1, "IT")));
        this.employees.add(new Employee(8, "Ashley Taylor", 70000.0, "2022-08-01", new Department(2, "HR")));
        this.employees.add(new Employee(9, "Matthew Thomas", 80000.0, "2022-09-01", new Department(3, "Finance")));
        this.employees.add(new Employee(10, "Olivia White", 60000.0, "2022-10-01", new Department(1, "IT")));
    }

	@Override
	public List<Employee> getAllEmployees() {
		System.out.println("Some data");
		System.out.println(employees);
		return this.employees;
	}
	
	 @Override
	    public Employee getEmployeeById(int id) {
	        // Find employee by ID
	        return employees.stream()
	                .filter(employee -> employee.getEmpId() == id)
	                .findFirst()
	                .orElse(null); // Return null if not found
	    }

}
