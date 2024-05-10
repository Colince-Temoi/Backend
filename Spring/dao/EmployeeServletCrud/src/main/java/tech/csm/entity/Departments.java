package tech.csm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
public class Departments implements Serializable {
	@Id
	@Column(name = "dept_id")
	private Integer deptId;
	
	private String name;
}
