package tech.csm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import tech.csm.entity.Employees;
import tech.csm.util.DBUtil;

public class EmployeeDaoImpl implements EmployeeDao {

	@Override
	public String saveEmp(Employees emp) {
		Session ses = DBUtil.getSessionFactory().openSession();
		Transaction tx = ses.beginTransaction();		
		ses.saveOrUpdate(emp);
		String res="1 emp saved with id: "+ses.getIdentifier(emp);
		tx.commit();
		ses.close();
		return res;
	}

	@Override
	public List<Employees> getAllEmps() {
		List<Employees> empList=null;
		final String seQuery="from Employees where isDelete='NO'";
		Session ses = DBUtil.getSessionFactory().openSession();
		Query<Employees> qr = ses.createQuery(seQuery);		
		empList=qr.list();		
		ses.close();
		return empList;
	}

	@Override
	public String deleteEmpById(Integer eId) {
		Session ses = DBUtil.getSessionFactory().openSession();
		final String seQuery="update Employees e set e.isDelete='YES' where e.employeeId=:empID";
		Query qr = ses.createQuery(seQuery);
		qr.setParameter("empID", eId);
		Transaction tx = ses.beginTransaction();
		qr.executeUpdate();
		String res="1 emp deleted with Id: "+eId;
		tx.commit();
		ses.close();
		return res;
	}

	@Override
	public Employees getEmpById(Integer eId) {
		Employees e=null;
		Session ses = DBUtil.getSessionFactory().openSession();
		e=ses.get(Employees.class, eId);
		ses.close();
		return e;
	}

}
