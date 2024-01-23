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
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity @Getter @Setter @ToString
@Table(name = "t_emp")
public class Employees implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "emp_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer employeeId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "hire_date")
	private Date hireDate;
	
	@Column(name = "salary")
	private Double salary;
	
	@Column(name = "employment_type")
	private String employmentType;
	
//	Means Many employees belong to one department
	@ManyToOne
	@JoinColumn(name = "dept_id")
	private Departments departments;
	
	@Column(name = "is_deleted")
	private String isDelete;

}
