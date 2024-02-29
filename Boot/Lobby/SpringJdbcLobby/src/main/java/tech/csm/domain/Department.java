package tech.csm.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Department implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer deptId;
	private String deptName;

}
