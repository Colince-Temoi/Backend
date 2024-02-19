package tech.csm.service;

import java.util.List;

import tech.csm.domain.Course;

public interface CourseService {

	List<Course> FindAllCourses();

	Double getCourseFees(Integer courseId);

}
