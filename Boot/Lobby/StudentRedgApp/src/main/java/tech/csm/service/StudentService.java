package tech.csm.service;

import java.util.List;

import tech.csm.domain.Student;

public interface StudentService {

	String saveStudent(Student student);

	List<Student> findAllStudents();

}
