package tech.get_tt_right.domain;

import java.io.Serializable;

public class EmployeeVo implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	Primitive dependencies
	private String name;
	private String hire_date;
	private String salary;

//	Secondary depandencies
	private DepartmentVo department;

//	Getters and setters.

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHire_date() {
		return hire_date;
	}

	public void setHire_date(String hire_date) {
		this.hire_date = hire_date;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public DepartmentVo getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentVo department) {
		this.department = department;
	}

//	You will edit this toString method to incorporate department inofrmation.
	@Override
	public String toString() {
		return "EmployeeVo [name=" + name + ", hire_date=" + hire_date + ", salary=" + salary + ", department="
				+ department + "]";
	}

}
