package com.get_tt_right.beans;

public class Employee {

//	Bean Properties definition
	private int eno;
	private String ename;
	private float esal;
	private String eaddr;

//	Bean getXXX && setXXX methods.
	public int getEno() {
		return eno;
	}
	public void setEno(int eno) {
		this.eno = eno;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public float getEsal() {
		return esal;
	}
	public void setEsal(float esal) {
		this.esal = esal;
	}
	public String getEaddr() {
		return eaddr;
	}
	public void setEaddr(String eaddr) {
		this.eaddr = eaddr;
	}
//	Bean Business method.
	public void getEmployeeDetails() {
		System.out.println("Employee Details");
		System.out.println("---------------------");
		System.out.println("Employee Number: "+eno);
		System.out.println("Employee Name: "+ename);
		System.out.println("Employee Salary: "+esal);
		System.out.println("Employee Address: "+eaddr);
	}
}
