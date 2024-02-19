package tech.csm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.csm.domain.Course;

@Repository
public interface CourseRepo extends JpaRepository<Course, Integer> {

}
