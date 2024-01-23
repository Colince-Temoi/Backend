package tech.csm.dao;

import java.util.List;

import org.hibernate.Session;
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

}
