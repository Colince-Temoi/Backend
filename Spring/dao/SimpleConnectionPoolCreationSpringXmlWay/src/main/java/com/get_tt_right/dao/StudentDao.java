package com.get_tt_right.dao;

import java.util.List;

import com.get_tt_right.model.StudentModel;

public interface StudentDao {

	public Integer save(StudentModel student) throws Exception;
	public boolean update(StudentModel student) throws Exception;
	public boolean delete(StudentModel student) throws Exception;
	public StudentModel findByPk(Integer id) throws Exception;
	public StudentModel findByEmail(String email) throws Exception;
	public List<StudentModel> findAllStudents() throws Exception;
}
