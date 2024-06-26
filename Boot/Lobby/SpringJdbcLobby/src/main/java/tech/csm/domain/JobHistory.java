package tech.csm.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter @ToString
public class JobHistory {
	
	private Integer employeeId;
	private Date startDate;
	private Date endDate;
	private String jobId;
	private Integer departmentId;

}
