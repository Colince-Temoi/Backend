package tech.csm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import tech.csm.entity.Employees;
import tech.csm.util.DBUtil;

public class EmployeeDaoImpl implements EmployeeDao {

	@Override
	public List<Employees> getAllEmps() {
//		Create a store to store all the employees
		List<Employees> empList=null;
//		Prepare a query string
		final String seQuery="from Employees where isDelete='NO'";
//		Get Session object
		Session ses = DBUtil.getSessionFactory().openSession();
//		Execute query
		Query<Employees> qr = ses.createQuery(seQuery);		
//		Store the returned employees in the store
		empList=qr.list();		
//		Close Session
		ses.close();
		return empList;
	}

	@Override
	public String saveEmp(Employees emp) {
//		Get session object
		Session ses = DBUtil.getSessionFactory().openSession();
//		Start transaction
		Transaction tx = ses.beginTransaction();
		
//		Save you Employee object
		ses.saveOrUpdate(emp);
		
//		Prepare success message
		String res="1 emp saved with id: "+ses.getIdentifier(emp);
//		Perform a commmit
		tx.commit();
		ses.close();
		return res;
	}

	/*I am doing update with query. Not with Session
	 * If you will do it with Session:
	 * 1. Get
	 * 2. Modify
	 * 3. Merge
	 * You can also do in this way.
	 * */
	@Override
	public String deleteEmpById(Integer eId) {
//		Get Session object
		Session ses = DBUtil.getSessionFactory().openSession();
		
//		Prepare query String
		final String upQuery="update Employees e set e.isDelete='YES' where e.employeeId=:empID";
		
//		Execute the query
		Query qr = ses.createQuery(upQuery);
		
//		Set the input value to empID
		qr.setParameter("empID", eId);
		
//		Begin Transaction
		Transaction tx = ses.beginTransaction();
		
//		Execute your query
		qr.executeUpdate();
		
//		Return the suitable message to the caller
		String res="1 emp deleted with Id: "+eId;
		tx.commit();
		ses.close();
		return res;
	}

	@Override
	public Employees getEmpById(Integer eId) {
//		Create store to receive the Employee gotten by Id
		Employees e=null;
//		Get the Session object
		Session ses = DBUtil.getSessionFactory().openSession();
//		This code will return the employee object by the passed id input.
		e=ses.get(Employees.class, eId);
		ses.close();
		return e;
	}

}
