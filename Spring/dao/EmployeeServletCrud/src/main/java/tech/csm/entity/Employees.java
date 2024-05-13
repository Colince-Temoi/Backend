package tech.csm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity @Getter @Setter @ToString
public class Employees implements Serializable {
	@Id
	@Column(name = "emp_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer employeeId;
	
	private String name;
	@Column(name = "hire_date")
	private Date hireDate;
	
	private Double salary;
	@Column(name = "employment_type")
	private String employmentType;
	
	@ManyToOne
	@JoinColumn(name = "dept_id")
	private Departments departments;
	@Column(name = "is_delete")
	private String isDelete;

}
