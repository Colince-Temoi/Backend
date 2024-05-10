package tech.csm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import tech.csm.entity.Departments;
import tech.csm.util.DBUtil;

public class DepartmentDaoImpl implements DepartmentDao {

	@Override
	public List<Departments> getAllDepartments() {
		final String seQuery="from Departments";
		List<Departments> deptList=null;
		Session ses = DBUtil.getSessionFactory().openSession();
		Query<Departments> qr = ses.createQuery(seQuery);
		deptList=qr.list();
		ses.close();
		return deptList;
	}

}
