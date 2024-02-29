package tech.csm.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter @ToString
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer employeeId;
	private String lastName;
	private Double Salary;
	private String jobId;
	private Date hireDate;
	
	private Department department;

}
