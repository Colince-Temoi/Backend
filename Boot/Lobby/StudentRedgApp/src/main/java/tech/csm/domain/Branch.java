package tech.csm.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class Branch implements Serializable {
	
	@Id
	@Column(name = "branch_id")
	private Integer branchId;
	
	@Column(name = "branch_name")
	private String branchName;

}
