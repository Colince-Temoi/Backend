package com.get_tt_right.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.get_tt_right.model.StudentModel;

import lombok.Setter;

/*
 * If you want to go through ComboPooledDataSource or DriverManagerDataSource or Hikari or whatever DS implementation class:
 * 1. Prepare the respective object
 * 2. Inject it into this DataSource interface reference and utilize that.
 * */

@Setter
public class StudentDaoImpl implements StudentDao {

	private SessionFactory sessionFactory;
//	private HibernateTemplate ht;

	public Integer save(StudentModel student) {
//		Integer i = (Integer) ht.save(student);
//		
//		return i;

		Integer studentId = null;

		Session ses = sessionFactory.openSession();

		// Begin a transaction
		org.hibernate.Transaction tx = ses.beginTransaction();
		// Perform operation: save the student object
		studentId = (Integer) ses.save(student);
		// Commit the transaction
		tx.commit();
		ses.close();

		return studentId;

	}
//
//	public boolean update(StudentModel student) throws Exception {
////		Get Connection object
//		Connection con = ds.getConnection();
//
////		update logic will go here
//		System.out.println("Connection established: " + con + " +++ Inside update method");
//
////		Close Connection
//		con.close();
//
//		return false;
//	}
//
//	public boolean delete(StudentModel student) throws Exception {
////		Get Connection object
//		Connection con = ds.getConnection();
//
////		delete logic will go here
//		System.out.println("Connection established: " + con + " +++ Inside delete method");
//
////		Close Connection
//		con.close();
//
//		return false;
//	}
//
//	public StudentModel findByPk(Integer id) throws Exception {
////		Get Connection object
//		Connection con = ds.getConnection();
//
////		findById logic will go here
//		System.out.println("Connection established: " + con + " +++ Inside findByPk method");
//
////		Close Connection
//		con.close();
//
//		return null;
//	}
//
//	public StudentModel findByEmail(String email) throws Exception {
//
////		Get Connection object
//		Connection con = ds.getConnection();
//
////		findByEmail logic will go here
//		System.out.println("Connection established: " + con + " +++ Inside findByEmail method");
//
////		Close Connection
//		con.close();
//
//		return null;
//	}
//
//	public List<StudentModel> findAllStudents() throws Exception {
////		Get Connection object
//		Connection con = ds.getConnection();
//
////		findAll logic will go here
//		System.out.println("Connection established: " + con + " +++ Inside findAll method");
//
////		Close Connection
//		con.close();
//
//		return null;
//	}

}
