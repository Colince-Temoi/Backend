package tech.get_tt_right.domain;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	Primitive dependencies
	private String name;
	private Date hire_date;
	private Double salary;

//	Secondary depandencies
	private Department department;
	
//	Getters and setters.

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getHire_date() {
		return hire_date;
	}

	public void setHire_date(Date date) {
		this.hire_date = date;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", hire_date=" + hire_date + ", salary=" + salary + ", department="
				+ department + "]";
	}

}
