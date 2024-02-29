package com.get_tt_right.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.sql.DataSource;

import com.get_tt_right.model.StudentModel;

import lombok.Setter;

/*
 * If you want to go through ComboPooledDataSource or DriverManagerDataSource or Hikari or whatever DS implementation class:
 * 1. Prepare the respective object
 * 2. Inject it into this DataSource interface reference and utilize that.
 * */

@Setter
public class StudentDaoImpl implements StudentDao {

//	In this interface reference we can inject any DS implementation class-At the moment I have injected BDS CP
	private DataSource ds;

	public Integer save(StudentModel student) throws Exception {
		PreparedStatement pstmt = null;
//		Get Connection object
		Connection con = ds.getConnection();
//		Save logic will go here
		// Prepare the SQL statement

		System.out.println(student);
		String sql = "INSERT INTO student (id, name, email, address) VALUES (?, ?, ?, ?)";
		pstmt = con.prepareStatement(sql);

		// Set parameters for PreparedStatement
		pstmt.setInt(1, student.getSId());
		pstmt.setString(2, student.getName());
		pstmt.setString(3, student.getEmail());
		pstmt.setString(4, student.getAddress());

		// Execute the PreparedStatement
		Integer ra = pstmt.executeUpdate();

//		Close Connection
		con.close();
		return ra;
	}

	public boolean update(StudentModel student) throws Exception {
//		Get Connection object
		Connection con = ds.getConnection();

//		update logic will go here
		System.out.println("Connection established: " + con + " +++ Inside update method");

//		Close Connection
		con.close();

		return false;
	}

	public boolean delete(StudentModel student) throws Exception {
//		Get Connection object
		Connection con = ds.getConnection();

//		delete logic will go here
		System.out.println("Connection established: " + con + " +++ Inside delete method");

//		Close Connection
		con.close();

		return false;
	}

	public StudentModel findByPk(Integer id) throws Exception {
//		Get Connection object
		Connection con = ds.getConnection();

//		findById logic will go here
		System.out.println("Connection established: " + con + " +++ Inside findByPk method");

//		Close Connection
		con.close();

		return null;
	}

	public StudentModel findByEmail(String email) throws Exception {

//		Get Connection object
		Connection con = ds.getConnection();

//		findByEmail logic will go here
		System.out.println("Connection established: " + con + " +++ Inside findByEmail method");

//		Close Connection
		con.close();

		return null;
	}

	public List<StudentModel> findAllStudents() throws Exception {
//		Get Connection object
		Connection con = ds.getConnection();

//		findAll logic will go here
		System.out.println("Connection established: " + con + " +++ Inside findAll method");

//		Close Connection
		con.close();

		return null;
	}

}

