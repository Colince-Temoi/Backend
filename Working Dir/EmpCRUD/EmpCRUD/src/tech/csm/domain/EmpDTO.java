package tech.csm.domain;

import java.io.Serializable;

public class EmpDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2231382798527102625L;
	private Integer empId;
	private String name;
	private Double Sal;
	public EmpDTO(Integer empId, String name, Double sal) {
		super();
		this.empId = empId;
		this.name = name;
		Sal = sal;
	}
	public EmpDTO() {
		super();
	}
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getSal() {
		return Sal;
	}
	public void setSal(Double sal) {
		Sal = sal;
	}
	@Override
	public String toString() {
		return "Emp [empId=" + empId + ", name=" + name + ", Sal=" + Sal + "]";
	}
}
