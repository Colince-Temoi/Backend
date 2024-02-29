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
@ToString(exclude = {"branch", "addresses"}) // Exclude fields causing cyclic references
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

/*
 * We are using CascadeType.ALL because we will also be performing CRUD
 * operations to Address table. The other tables we already have data
 * pre-inserted in the DB end before we even began building this application. In
 * Summary
 * 
 * 
 * This entity class defines the structure of a 'Student' within your
 * application's data model. It includes fields for basic student information as
 * well as relationships to other entities (Branch, Course, Address) to
 * represent real-world associations between these concepts.
 * 
 * 
 * The java.lang.StackOverflowError you're encountering is due to a cyclic dependency in your toString() methods between your Student and Branch entities. This creates an infinite loop during the serialization process.


Understanding the Issue:
When you call toString() on a Student, it tries to include the Branch information. The Branch's toString() method, in turn, attempts to include the list of Students, which calls the Student's toString() again, and so forth, leading to an infinite recursive call and stack overflow.


Solutions:


Exclude Problematic Fields from toString():
As you've already discovered, commenting out @ToString is the simplest solution. However, if you still want a toString() method for debugging or logging purposes, you can customize it using Lombok's @ToString.Exclude to break the cyclic dependency. For instance:

@ToString(exclude = {"branch", "addresses"}) // Exclude fields causing cyclic references

Break the Cyclic Dependency:

Unidirectional Relationship: If the relationship between Student and Branch doesn’t need to be bidirectional, removing one side can prevent the cyclic reference. In many cases, a Student having a Branch is enough, and the Branch doesn’t need a direct reference back to its list of Students.
DTO Projection: For complex scenarios, consider using DTOs (Data Transfer Objects) and project only necessary fields when fetching data for your views. This way, you can control which fields are serialized and avoid the cyclical issue altogether.

Handle in the JSON Serializer:
If you are using a JSON Serializer like Jackson, you can configure it to ignore specific fields or handle cyclic references gracefully using annotations like @JsonIgnore and @JsonManagedReference and @JsonBackReference for bi-directional relationships.

Additional Considerations:


Consider the depth of your object graph and the potential for circular references. Tools like Hibernate can sometimes mask these issues until they manifest in specific scenarios like serialization.
Be cautious with bidirectional relationships, especially when combined with automatic toString() generation.

Would you like me to elaborate further on DTO projection or JSON serialization configurations for handling cyclic dependencies?
 */
