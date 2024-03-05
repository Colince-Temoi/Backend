package tech.csm.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Department implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonProperty("dep_id")
	private Integer departmentId;

	@JsonProperty("dept_name")
	private String departmentName;
}
