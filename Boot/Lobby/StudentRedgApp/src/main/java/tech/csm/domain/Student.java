package tech.csm.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "t_student_master")
public class Student implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "roll_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rollNo;

	@Column(name = "name")
	private String studentName;

	@Column(name = "email")
	private String email;

	@Column(name = "dob")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;

	@Column(name = "cgpa")
	private Double cgpa;

	@Column(name = "yroa")
	private Integer yearOfAddmission;

//	@ManyToOne(): Defines a many-to-one relationship: many students can belong to one branch.
	@ManyToOne()
//	@JoinColumn(name = "branch_id"): Specifies that the branch_id column in the student table acts as a foreign key to the Branch table.
	@JoinColumn(name = "branch_id")
	private Branch branch;

//	@ManyToMany: Defines a many-to-many relationship: a student can have multiple courses, and a course can have multiple students.
	@ManyToMany
//	joinColumns = @JoinColumn(name = "roll_no"): The column in the join table referencing the student's rollNo.
//	inverseJoinColumns = @JoinColumn(name = "course_id"): The column referencing the course's ID.
	@JoinTable(name = "t_student_course", joinColumns = @JoinColumn(name = "roll_no"), inverseJoinColumns = @JoinColumn(name = "course_id"))
//	A List<Course> holding the courses the student is enrolled in.
	private List<Course> courses;

//	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL): Defines a one-to-many relationship: one student can have multiple addresses.
//	mappedBy = "student": Indicates that the student property in the Address entity is responsible for the relationship.
	@OneToMany(mappedBy = "student")
//	A List<Address> holding the student's addresses.
	private List<Address> addresses;
}

/*We are  using CascadeType.ALL because we will also be performing CRUD operations to Address table.
 * The other tables we already have data pre-inserted in the DB end before we even began building this application.
 * In Summary


This entity class defines the structure of a 'Student' within your application's data model. 
It includes fields for basic student information as well as relationships to other entities (Branch, Course, Address) to represent real-world associations between these concepts.
 * */
