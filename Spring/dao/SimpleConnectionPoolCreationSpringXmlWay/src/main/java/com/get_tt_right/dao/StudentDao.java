package com.get_tt_right.dao;

import java.sql.SQLException;

import com.get_tt_right.model.StudentModel;

public interface StudentDao {

	public Integer save(StudentModel student) throws SQLException;
}
