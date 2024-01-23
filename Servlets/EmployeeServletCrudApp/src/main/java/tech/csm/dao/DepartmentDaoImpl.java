package tech.csm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import tech.csm.entity.Departments;
import tech.csm.util.DBUtil;

public class DepartmentDaoImpl implements DepartmentDao {

	@Override
	public List<Departments> getAllDepartments() {
//		Prepare query to fetch all the departments
		final String seQuery="from Departments";
		
//		Prepare Container to hold the returned departments
		List<Departments> deptList=null;
//		Get Session object
		Session ses = DBUtil.getSessionFactory().openSession();
		
//		Execute the query
		Query<Departments> qr = ses.createQuery(seQuery);
		
//		Store the returned list of departments in the container reference
		deptList=qr.list();
		
//		Close session
		ses.close();
		
//		List of departments
		return deptList;
	}

}
