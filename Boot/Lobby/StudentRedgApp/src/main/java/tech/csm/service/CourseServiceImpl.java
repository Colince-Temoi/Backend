package tech.csm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.csm.domain.Course;
import tech.csm.repo.CourseRepo;

@Service
public class CourseServiceImpl implements CourseService {
	
	@Autowired
	private CourseRepo courseRepo;

	@Override
	public List<Course> FindAllCourses() {
		
		return courseRepo.findAll();
	}

	@Override
	public Double getCourseFees(Integer courseId) {
	    Course course = courseRepo.findById(courseId).orElseThrow();
	    Double fees = course.getFees();
	    return fees;
	}

}
