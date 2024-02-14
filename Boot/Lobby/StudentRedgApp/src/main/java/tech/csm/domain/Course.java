package tech.csm.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class Course implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "course_id")
	private Integer courseId;

	@Column(name = "course_title")
	private String courseName;

	@Column(name = "fees")
	private Double fees;

	/*
	 * @ManyToMany(mappedBy = "courses") private List<Student> students;
	 */
}
