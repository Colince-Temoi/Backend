package tech.csm.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Department implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer departmentId;

	private String departmentName;
}
