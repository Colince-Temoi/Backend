package tech.csm.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("emp_id")
	private int empId;

	@JsonProperty("emp_name")
	private String empName;

	private double sal;

	@JsonProperty("hire_date")
	private String hireDate;

	private Collage department;
}
