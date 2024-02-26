package com.get_tt_right.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.get_tt_right.model.StudentModel;

import lombok.Setter;
@Setter
public class StudentDaoImpl implements StudentDao {

	private DataSource ds;

	public Integer save(StudentModel student) throws SQLException {
//		Save logic will go here
		System.out.println(ds.getConnection());
		return 1;
	}
	
	
}
