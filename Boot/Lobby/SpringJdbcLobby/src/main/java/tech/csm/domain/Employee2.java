package tech.csm.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Employee2 implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer employeeId;
	private String firstName;
	private String email;
	private String jobId;
	private Date hireDate;
	private Double salary;

}
