package tech.get_tt_right.domain;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	// Primitive dependencies
	private Integer empId;
	private String name;
	private Date hire_date;
	private Double salary;
	private String isDeleted;

//	Secondary depandencies
	private Department department;

//	Getters and setters.

	public String getName() {
		return name;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
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

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

//	The isDeleted value should not be shown to the user; so make sure to omit it from the toString.

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", name=" + name + ", hire_date=" + hire_date + ", salary=" + salary
				+ ", isDeleted=" + isDeleted + ", department=" + department + "]";
	}

}
